function passExtentToApp(extent) {
  var url = "http://extent/" + extent;
  document.location.href = url;
}

function passZoomInValToApp(zoomVal) {
  var url = "http://zoomIn/"+zoomVal;
  document.location.href = url;
}

function passZoomOutValToApp(zoomVal) {
  var url = "http://zoomOut/"+zoomVal;
  document.location.href = url;
}
function passDefaultZoomToApp() {
  var url = "http://DefaultZoom";
  document.location.href = url;
}

function passPointIndexToApp(index, name) {
  var url = "http://" + index + "_" + name;
  document.location.href = url;
}

var element = document.getElementById("popup");
var popupVisible = false;
var pointIndex = null;
var pointName = null;

var interactions = ol.interaction.defaults({
  altShiftDragRotate: false,
  pinchRotate: false,
});

const zoomInButton = document.createElement("div");
zoomInButton.innerHTML =
  '<img src="file:///android_asset/ZoomIcon/zoom_in.svg" class="ol-zoom-in-icon">';
zoomInButton.className = "ol-zoom-in-container";
const zoomOutImage = document.createElement("img");
zoomOutImage.src = "file:///android_asset/ZoomIcon/zoom_out.svg";
zoomOutImage.className = "ol-zoom-out-icon";
const zoom = new ol.control.Zoom({
  zoomInLabel: zoomInButton,
  zoomOutLabel: zoomOutImage,
});

var map = new ol.Map({
  controls: [zoom],
  interactions: interactions,
  target: "map",
  layers: [layer, polygonVectorLayer, pinVectorLayer],
  view: new ol.View({
    maxResolution: mapTileGrid.getResolution(mapMinZoom),
    extent: mapExtent,
    showFullExtent: true,
  }),
});

var element = document.getElementById("popup");
var previousZoom = map.getView().getZoom();

var popup = new ol.Overlay({
  element: element,
  positioning: "bottom-center",
  stopEvent: false,
  offset: [0, -10],
});
map.addOverlay(popup);

// display popup on click;
map.on("click", function (evt) {
  if (evt.originalEvent && evt.originalEvent.target.id === "title-text-id") {
    passPointIndexToApp(pointIndex, pointName);
    dropElement();
  } else if (!popupVisible) {
    feature = map.forEachFeatureAtPixel(evt.pixel, function (feature) {
      return feature;
    });

    if (feature) {
      popupVisible = true;
      var coordinates;
      if (feature.getGeometry() instanceof ol.geom.Point) {
        coordinates = feature.getGeometry().getCoordinates();
      } else {
        coordinates = evt.coordinate;
      }
      popup.setPosition(coordinates);

      var content = '<div id="container-id" class="container"></div>';
      $(element).popover({
        placement: "top",
        html: true,
        content: content,
      });

      $(element).popover("show");
      $(".popover").css("background-color", feature.get("color"));
      $(".popover-content").css("color", "white");
      $(".popover.top  .arrow").css("border-top-color", feature.get("color"));

      var elem = document.getElementById("container-id");
      elem.innerHTML =
        '<div class="number"><img id="icon-1-id" class="icon-1" src="' +
        feature.get("issueStatus") +
        '"><span id="number-text-id" class="number-text">' +
        feature.get("numb") +
        '</span></div><a href="http://' +
        feature.get("indd") +
        "_" +
        '" class="a-link"><div id="text-id" class="text__wrapper"><div id="text-content" class="text">' +
        feature.get("name") +
        "</div></div></a>";
      $(".a-link").css("height", "60px");

      pointIndex = feature.get("indd");
      pointName = feature.get("name");
    } else {
      dropElement();
    }
  } else {
    dropElement();
  }
});

function dropElement() {
  popupVisible = false;
  $(element).popover("dispose");
}

map.on("moveend", function onMoveEnd(evt) {
  var map = evt.map;
  var extent = map.getView().calculateExtent(map.getSize());
  var bottomLeft = ol.proj.toLonLat(ol.extent.getBottomLeft(extent));
  var topRight = ol.proj.toLonLat(ol.extent.getTopRight(extent));

   var currentZoom = map.getView().getZoom();
      passZoomInValToApp(currentZoom)
  if (currentZoom > previousZoom) {
      passZoomInValToApp(currentZoom)
  } else if (currentZoom < previousZoom) {
      passZoomOutValToApp(currentZoom)
  } else if (currentZoom === map.getView().getMinZoom()) {
      passDefaultZoomToApp()
  }
//  passZoomInValToApp(currentZoom)
  previousZoom = currentZoom;
  passExtentToApp(extent);
});

map.on("movestart", function onMoveStart(evt) {
  dropElement()
});
map.getView().fit(beginExtent, map.getSize());
