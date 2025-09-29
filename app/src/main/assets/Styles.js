const pinStyle = [
    new ol.style.Style({
        image: new ol.style.Circle({
            fill: new ol.style.Fill({
                color: color
            }),
            stroke: new ol.style.Stroke({
                color: 'white',
                width: 2
            }),
            radius: 7,
            })
        }),
    new ol.style.Style({
        image: new ol.style.Circle({
            radius: 12,
            fill: new ol.style.Fill({
                color: [255,255,255, 0.01]
            }),
        }),
        zIndex: Infinity,
    }),
];

const polygonStyle = [
    new ol.style.Style({
        image: new ol.style.Circle({
            radius: 7,
            fill: new ol.style.Fill({
                color: color,
            }),
            stroke: new ol.style.Stroke({
                width: 2,
                color: 'white',
            }),
        }),
    }),
    new ol.style.Style({
        stroke: new ol.style.Stroke({
            color: color,
            width: 3,
        }),
        fill: new ol.style.Fill({
            color: fillColor,
        }),
    }),
    new ol.style.Style({
            image: new ol.style.Circle({
                radius: 12,
                fill: new ol.style.Fill({
                    color: [255,255,255, 0.01]
                }),
            }),
            zIndex: Infinity,
     }),
];

function setDrawingPolygonStyle(feature) {
  return drawingPolygonStyles[feature.getGeometry().getType()];
}

drawingPolygonStyles = {
    Point: new ol.style.Style({
        image: new ol.style.Circle({
            radius: 6.5,
            stroke: new ol.style.Stroke({
                color: 'white',
                width: 2
            }),
            fill: new ol.style.Fill({
            color: color,
            })
        }),
    }),
    LineString: new ol.style.Style({
        stroke: new ol.style.Stroke({
            color: color,
            width: 3,
        }),
    }),
    Polygon: new ol.style.Style({
        fill: new ol.style.Fill({
            color: fillColor,
        }),
    }),
};

function createDraggingStyle(color, strokeColor) {

    const pinActiveSize = 46;
    const pinActiveSizeY = 100;
    const canvas = document.createElement('canvas');

    canvas.width = pinActiveSize;
    canvas.height = pinActiveSizeY;

    const context = canvas.getContext('2d');

    context.fillStyle = color;
    context.strokeStyle = strokeColor;
    context.lineWidth = 30;
    context.beginPath();
    context.arc(23, 30, 7, 0, 2 * Math.PI);
    context.fill();
    context.stroke();

    return new ol.style.Style({
        image: new ol.style.Icon(({
            src: canvas.toDataURL(),
            scale: 1,
            anchor: [0.48, 0.85],
            opacity: 1,
            imgSize: [pinActiveSize, pinActiveSizeY],
        })),
        zIndex: Infinity,
    });
}
const pinDraggingStyle = createDraggingStyle(color, fillColor);