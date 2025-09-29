const POLYGON_MAX_COUNT = 30;
const PIN_MAX_COUNT = 30;

function setFeatureMode(isPolygonMode) {
    polygonMode = isPolygonMode;

    modify.setActive(polygonMode);
    translate.setActive(!polygonMode);
    if (polygons.length >= POLYGON_MAX_COUNT) {
        drawPolygon.setActive(false);
    } else {
        drawPolygon.setActive(polygonMode);
    }

    updateSwitchButtonView(isPolygonMode);
    if (polygonMode) {
        createPinSnackbar("createPolygonSnackbar");
    } else {
        createPinSnackbar("createPinSnackbar");
    }
}

function updateSwitchButtonView(activePolygon) {
    if (activePolygon) {
        sitePlanModeOptionPin.classList.remove('sitePlanModeOption--active');
        sitePlanModeOptionPolygon.classList.add('sitePlanModeOption--active');
        document.getElementById("pinIcon").src = "file:///android_asset/MapIcons/pin_inactive.svg";
        document.getElementById("polygonIcon").src = "file:///android_asset/MapIcons/polygon_active.svg";
    } else {
        sitePlanModeOptionPin.classList.add('sitePlanModeOption--active');
        sitePlanModeOptionPolygon.classList.remove('sitePlanModeOption--active');
        document.getElementById("pinIcon").src = "file:///android_asset/MapIcons/pin_active.svg";
        document.getElementById("polygonIcon").src = "file:///android_asset/MapIcons/polygon_inactive.svg";

        let snackbar = document.getElementById("finishPolygonSnackbar");
        snackbar.classList.remove('showSnackbar');
    }
}

function passLocationToApp() {

    var pinPositions = [];
    pins.forEach(pin => {
        pinPositions.push(pin.getGeometry().getCoordinates() + "");
    });

    var polygonPositions = [];
    polygons.forEach(polygon => {
        var coords = polygon.getGeometry().getCoordinates()[0] + "";
        polygonPositions.push(coords);
    });

    Android.getLocation(pinPositions, polygonPositions);
}

function onPassGeomCleared() {
    let snackbar = document.getElementById("finishPolygonSnackbar");
    snackbar.classList.remove('showSnackbar');

    drawPolygon.setActive(false);
    setTimeout(() => {
        drawPolygon.setActive(polygonMode);
    }, 350);

    pins = [];
    polygons = [];

    pinVectorSource.clear();
    polygonVectorSource.clear();
    checkClearModeIcon();

    Android.clearLocation();
}

function passPolygonCreationMode(isCreating) {
    Android.blockButton(isCreating);
}

function createInteraction(pin) {

   translate.on('translatestart', function dragStart(evt) {
        let translatedPins = evt.features;

        let translatedPin;
        translatedPins.forEach( p => {
            translatedPin = p.getGeometry().getCoordinates();
        });

       if (translatedPin != null) {
           if (translatedPin[0] == pin.getGeometry().getCoordinates()[0]
             && translatedPin[1] == pin.getGeometry().getCoordinates()[1]) {
               pin.setStyle(pinDraggingStyle);
               pin.setId("moved");
           }
       }

       binVisibility(true);
   });

   translate.on('translateend', function dragged(event) {
       if (pin.getId() == "moved") {
           var coordinate = pin.getGeometry().getCoordinates();
           var pixel = map.getPixelFromCoordinate(coordinate);
           pixel[1] = pixel[1] - 54;
           const newCoordinates = map.getCoordinateFromPixel(pixel);
           pin.getGeometry().setCoordinates(newCoordinates);
           pin.setStyle(pinStyle);

           var insideMap = ol.extent.containsExtent(mapExtent, pin.getGeometry().getExtent());
           if (!insideMap) {
               fixCoordinates(pin);
           }

           if (pixel[0] > vwLeft && pixel[0] < (vwLeft + 60) && pixel[1] > (vhTop - 54) && pixel[1] < (vhTop + 70)) {
               deletePin(pin);
           }
           passLocationToApp();
       }
       binVisibility(false);
       pin.setId("");
       checkClearModeIcon();
   }, translate);
}

function binVisibility(visibility) {
    if (visibility) {
        bin.style.visibility = "visible";
    } else {
        bin.style.visibility = "hidden";
        bin.classList.remove('deletePin--hover');
    }
}

function fixCoordinates(pin) {
   var x = pin.getGeometry().getCoordinates()[0];
   var y = pin.getGeometry().getCoordinates()[1];
   pin.getGeometry().setCoordinates(checkXYCoordinate(x,y));
}

function fixPolygons() {
    polygons.forEach( polygon => {
        fixPolygonCoordinates(polygon);
    });
}

function fixPolygonCoordinates(polygon) {
    var coordinates = polygon.getGeometry().getCoordinates();
    for (let i = 0; i < coordinates[0].length; i++) {
        var x = coordinates[0][i][0];
        var y = coordinates[0][i][1];

        var newXY = checkXYCoordinate(x,y);
        coordinates[0][i][0] = newXY[0];
        coordinates[0][i][1] = newXY[1];
    }
    polygon.getGeometry().setCoordinates(coordinates);
}

function checkXYCoordinate(x, y) {
    var newX = x;
    var newY = y;

    if (x < 0) {
        newX = 1;
    } else if (x > mapExtent[2]) {
        newX = mapExtent[2] - 1;
    }
    if (y > 0) {
        newY = -1;
    } else if (y < mapExtent[1]) {
        newY = mapExtent[1] + 1;
    }

    return [newX, newY];
}

function deletePin(pin) {
    pins = pins.filter(function(el) { return el != pin; });
    pinVectorSource.removeFeature(pin);
}

function deletePolygon(polygon) {
    polygons = polygons.filter(function(el) { return el != polygon; });
    polygonVectorSource.removeFeature(polygon);
    if (polygons.length < POLYGON_MAX_COUNT) {
        drawPolygon.setActive(polygonMode);
    }
}

const vwLeft = (window.innerWidth/2) - 30;
const vhTop = window.innerHeight - 70;

const zoomInButton = document.createElement('div');
    zoomInButton.innerHTML = '<img src="file:///android_asset/ZoomIcon/zoom_in.svg" class="ol-zoom-in-icon">';
    zoomInButton.className = 'ol-zoom-in-container';
    const zoomOutImage = document.createElement('img');
    zoomOutImage.src = 'file:///android_asset/ZoomIcon/zoom_out.svg';
    zoomOutImage.className = 'ol-zoom-out-icon';
    const zoom = new ol.control.Zoom({
    zoomInLabel: zoomInButton,
    zoomOutLabel: zoomOutImage,
});

var interactions = ol.interaction.defaults({
   altShiftDragRotate:false,
   pinchRotate:false
});
var translate = new ol.interaction.Translate({
    layers: [pinVectorLayer],
    hitTolerance: 15,
});
var modify = new ol.interaction.Modify({
    source: polygonVectorSource,
    style: polygonStyle,
    condition: function (mapBrowserEvent) {
        return !mapBrowserEvent.dragging;
    },
});
var drawPolygon = new ol.interaction.Draw({
    source: polygonVectorSource,
    type: 'Polygon',
    style: feature => this.setDrawingPolygonStyle(feature),
    condition: function (mapBrowserEvent) {
        return !mapBrowserEvent.dragging;
    }
});
var snap = new ol.interaction.Snap({
    source: polygonVectorSource,
    pixelTolerance: 20,
});
var map = new ol.Map({
   controls: [zoom],
   interactions: interactions,
   target: 'map',
   layers: [ layer, polygonVectorLayer, pinVectorLayer ],
   view: new ol.View({
       maxResolution: mapTileGrid.getResolution(mapMinZoom),
       extent: mapExtent,
       showFullExtent: true
   }),
});

map.addInteraction(translate);
map.addInteraction(modify);
map.addInteraction(drawPolygon);
map.addInteraction(snap);
map.getView().fit(beginExtent, map.getSize());

modify.setActive(false);
drawPolygon.setActive(false);

pins.forEach(pin => {
   createInteraction(pin);
});

// Used to recognize if there was recently other actions
var isPolygonModified = false;
var isPolygonBeingDrawn = false;

var deleteArea = document.getElementById('deleteArea');
var deleteVertice = document.getElementById('deleteVertice');
var verticeCoords;
var verticeIndex = -1;
var polygonIndex = -1;

function onDeleteVerticeClick() {
    closePopups();

    if (polygonIndex == -1) {
        return;
    }

    var polygon = polygons[polygonIndex];

    if (verticeIndex != -1) {
        var coordinates = polygon.getGeometry().getCoordinates()[0];

        var newCoordinates = [];
        coordinates.splice(verticeIndex, 1);
        if (verticeIndex === 0) {
            coordinates.splice(coordinates.length - 1, 1);
            coordinates.push(coordinates[0]);
        }
        if (verticeIndex === (coordinates.length)) {
            coordinates.splice(0, 1);
            coordinates.push(coordinates[0]);
        }
        newCoordinates.push(coordinates);
        polygon.getGeometry().setCoordinates(newCoordinates);

        passLocationToApp();
    }
}

map.on("singleclick", function addNewFeatureOnMap(evt) {

   var latLong = ol.proj.transform(evt.coordinate, 'EPSG:3857', 'EPSG:4326');
   var pixel = map.getPixelFromCoordinate(evt.coordinate);
   var lat = latLong[0];
   var lng = latLong[1];

    if (!polygonMode) {
        if (pins.length >= PIN_MAX_COUNT) {
            createPinSnackbar("pinNumberExceeded");
            return;
        }

       closePopups();
       var newPin = new ol.Feature({
           geometry: new ol.geom.Point(ol.proj.transform([lat, lng], 'EPSG:4326', 'EPSG:3857'))
       });

       var insideMap = ol.extent.containsExtent(mapExtent, newPin.getGeometry().getExtent());
       if (insideMap) {
           createInteraction(newPin);
           pinVectorSource.addFeature(newPin);
           newPin.setStyle(pinStyle);
           pins.push(newPin);
           checkClearModeIcon();
           passLocationToApp();
       }
   } else {
        closePopups();
        const errorMargin = 20;

        if (isPolygonBeingDrawn) {
            return;
        }

        var foundPolygonVertice = false;
        for (let i = 0; i < polygons.length; i++) {

            const vertices = polygons[i].getGeometry().getCoordinates()[0];

            for (let j = 0; j < vertices.length; j++) {
                const vertice = vertices[j];
                const verticePixel = map.getPixelFromCoordinate(vertice);
                if (Math.abs(pixel[0] - verticePixel[0]) < errorMargin && Math.abs(pixel[1] - verticePixel[1]) < errorMargin) {
                    if (!isPolygonModified && !isPolygonBeingDrawn) {
                        foundPolygonVertice = true;
                        if (vertices.length > 4) {
                            verticeCoords = verticePixel;
                            verticeIndex = j;
                            polygonIndex = i;
                            deleteVertice.style.left = (pixel[0] - 60) + "px";
                            deleteVertice.style.top = (pixel[1] - 45) + "px";
                            deleteVertice.style.visibility = "visible";
                        }
                    }
                }
            }

            if (polygons[i].getGeometry().intersectsCoordinate(evt.coordinate)
                    && !isPolygonBeingDrawn && !isPolygonModified) {
                foundPolygonVertice = true;
                polygonIndex = i;
                deleteArea.style.left = (pixel[0] - 60) + "px";
                deleteArea.style.top = (pixel[1] - 45) + "px";
                deleteArea.style.visibility = "visible";
            }

        }

        if (polygons.length >= POLYGON_MAX_COUNT) {
           createPinSnackbar("polygonNumberExceeded");
           drawPolygon.setActive(false);
        } else {
            drawPolygon.setActive(!foundPolygonVertice);
        }
   }
});

function checkClearModeIcon() {
    var visibility = (pins.length > 0 || polygons.length > 0) ?
        "visible" : "hidden";
    clearAllOptionMenu.style.visibility = visibility;
}

function closePopups() {
    deleteArea.style.visibility = "hidden";
    deleteVertice.style.visibility = "hidden";
}

map.on("pointermove", function moveBin(event) {
   var pixel = event.pixel;
   if (pixel[0] > vwLeft && pixel[0] < (vwLeft + 60) && pixel[1] > vhTop && pixel[1] < (vhTop + 120)) {
       bin.classList.add('deletePin--hover');
   } else {
       bin.classList.remove('deletePin--hover');
   }
});

var modifiedPolygonIndex = -1;
var oldCoordinates;
function addPolygonInteraction() {

    drawPolygon.on('drawstart', (event) => {
        closePopups();
        setTimeout(() => {

            if (drawPolygon.getActive()) {
               createPinSnackbar("finishPolygonSnackbar");
               let snackbar = document.getElementById("createPolygonSnackbar");
                 snackbar.classList.remove('showSnackbar');
               isPolygonBeingDrawn = true;
               passPolygonCreationMode(true);
               modify.setActive(false);
            } else {
                let snackbar = document.getElementById("finishPolygonSnackbar");
                snackbar.classList.remove('showSnackbar');
            }
        }, 350);
    });

    drawPolygon.on('drawend', (event) => {
        if (polygonMode) {
            var newPolygon = event.feature;
            fixPolygonCoordinates(newPolygon);
            onDrawNewPolygon(newPolygon);
            newPolygon.setStyle(polygonStyle);
            checkClearModeIcon();
        }
        let snackbar = document.getElementById("finishPolygonSnackbar");
        snackbar.classList.remove('showSnackbar');
        drawPolygon.setActive(false);
        setTimeout(() => {
           if (polygons.length <= POLYGON_MAX_COUNT) {
               drawPolygon.setActive(true);
           }
           modify.setActive(true);
           isPolygonBeingDrawn = false;
           passPolygonCreationMode(false);
        }, 400);
    });

    modify.on('modifystart', (event) => {
        closePopups();
        isPolygonModified = true;

        modifiedPolygonIndex = -1;
        var errorMargin = 20;
        var pixel = map.getPixelFromCoordinate(event.mapBrowserEvent.coordinate);
        for (let i = 0; i < polygons.length; i++) {
            const vertices = polygons[i].getGeometry().getCoordinates()[0];
            for (let j = 0; j < vertices.length; j++) {
                const vertice = vertices[j];
                const verticePixel = map.getPixelFromCoordinate(vertice);
                if (Math.abs(pixel[0] - verticePixel[0]) < errorMargin && Math.abs(pixel[1] - verticePixel[1]) < errorMargin) {
                    modifiedPolygonIndex = i;
                    oldCoordinates = vertices;
                }
            }
        }

        if (modifiedPolygonIndex > -1) {
            if (polygons[modifiedPolygonIndex].getGeometry().getCoordinates()[0].length > 4) {
                binVisibility(true);
            }
        }

    });

    modify.on('modifyend', (event) => {
        if (!isPolygonBeingDrawn) {
            fixPolygons();
        }

        if (bin.classList.contains("deletePin--hover")) {
            deletePolygonVertex();
        }

        passLocationToApp();

        binVisibility(false);
        setTimeout(() => {
            isPolygonModified = false;
        }, 350);

        modify.setActive(false);
        setTimeout(() => {
            modify.setActive(true);
        }, 200);
    });
}

function deletePolygonVertex() {

    if (modifiedPolygonIndex < 0) return;

    let coords = polygons[modifiedPolygonIndex].getGeometry().getCoordinates()[0];
    if (coords.length < 5) return;

    var newCoordinates = [];

    var index;
        for (let i = 0; i < oldCoordinates.length; i++) {
            if (oldCoordinates[i].toString() != coords[i].toString()) {
                index = i;
            }
        }

        coords.splice(index, 1);

        if (index === 0) {
            coords.splice(coords.length - 1, 1);
            coords.push(coords[0]);
        }

        if (index === (coords.length)) {
            coords.splice(0, 1);
            coords.push(coords[0]);
        }

        newCoordinates.push(coords);

        polygons[modifiedPolygonIndex].getGeometry().setCoordinates(newCoordinates);
}

function onDrawNewPolygon(newPolygon) {
    polygons.push(newPolygon);
    passLocationToApp();
}

addPolygonInteraction();
showFirstMessage(polygonMode);
updateSwitchButtonView(polygonMode);

function showFirstMessage(polygonMode) {
    if (polygonMode) {
        createPinSnackbar("editPolygonSnackbar");
    } else {
        createPinSnackbar("createPinSnackbar");
    }
}

function createPinSnackbar(id) {
  // Get the snackbar DIV
  var snackbar = document.getElementById(id);
  // Add the "show" class to DIV
  snackbar.classList.add('showSnackbar');

  if (id !== "finishPolygonSnackbar") {
      // After 2.8 seconds, remove the show class from DIV
      setTimeout(function(){
        snackbar.classList.remove('showSnackbar');
      }, 5000);
  }
}