function onPinModeClick() {
    closePopups();
    if (!polygonMode) return;
    setFeatureMode(false);
}

function onPolygonModeClick() {
    closePopups();
    if (polygonMode) return;
    setFeatureMode(true);
}

function onDeleteAreaClick() {
    closePopups();
    deletePolygonAreaModal.style.display="block";
}

function onClearAllModeClick() {
    closePopups();
    deleteLocationsModal.style.display="block";
}

// Get the modal
var deletePolygonAreaModal = document.getElementById("deletePolygonAreaModal");
var deleteLocationsModal = document.getElementById("deleteLocationsModal");

// Get the button that confirms the modal
var cancelDeletePolygonAreaBtn = document.getElementById("cancelDeleteArea");
var confirmDeletePolygonAreaBtn = document.getElementById("confirmDeleteArea");
var cancelDeleteLocationsBtn = document.getElementById("cancelDeleteLocations");
var confirmDeleteLocationsBtn = document.getElementById("confirmDeleteLocations");

cancelDeletePolygonAreaBtn.onclick = function() {
    deletePolygonAreaModal.style.display = "none";
}

confirmDeletePolygonAreaBtn.onclick = function() {
    deletePolygonAreaModal.style.display = "none";
    if (polygonIndex > -1)
        deletePolygon(polygons[polygonIndex])
    passLocationToApp();
}

cancelDeleteLocationsBtn.onclick = function() {
    deleteLocationsModal.style.display = "none";
}

confirmDeleteLocationsBtn.onclick = function() {
    deleteLocationsModal.style.display = "none";
    onPassGeomCleared();
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function(event) {
  if (event.target == deletePolygonAreaModal) {
    deletePolygonAreaModal.style.display = "none";
  } else if (event.target == deleteLocationsModal) {
    deleteLocationsModal.style.display = "none";
  }
}
