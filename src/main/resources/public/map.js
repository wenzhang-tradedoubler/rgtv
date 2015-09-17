// This example adds an animated symbol to a polyline.

var map;
var markers = [];

function initMap() {
	//Style map.
	var customMapType = new google.maps.StyledMapType([
      {
        stylers: [
          {gamma: 0.68 },
          {invert_lightness: false },
          {lightness: -50 },
          {saturation: -60 }
        ]
      },
      {
        elementType: 'landscape',
        stylers: [
        {visibility: 'on'},
        {lightness: -50}
        ]
      },
      {
         featureType: 'poi',
         stylers: [
           { "visibility": "off" }
         ]
      },
      {
        featureType: 'water',
        stylers: [
        {color: '#31373E'},
        {lightness: -30}
        ]
      }
    ], {
      name: 'Custom Style'
  });
  var customMapTypeId = 'custom_style';
  
  map = new google.maps.Map(document.getElementById('map'), {
    center: {lat: 59.341112, lng: 18.064447},
    zoom: 3,
    mapTypeId: [google.maps.MapTypeId.TERRAIN, customMapTypeId]
  });
  
  map.mapTypes.set(customMapTypeId, customMapType);
  map.setMapTypeId(customMapTypeId);

  // Define the symbol, using one of the predefined paths ('CIRCLE')
  // supplied by the Google Maps JavaScript API.
  var lineSymbol = {
    path: google.maps.SymbolPath.CIRCLE,
    scale: 8,
    strokeColor: '#393'
  };

  // Create the polyline and add the symbol to it via the 'icons' property.
  var line = new google.maps.Polyline({
    path: [{lat: 22.291, lng: 153.027}, {lat: 18.291, lng: 153.027}],
    icons: [{
      icon: lineSymbol,
      offset: '100%'
    }],
    map: map
  });

  animateCircle(line);

  for(var i=0; i< 100; i++){
  	//delay 100ms.
  	dropMarkerTimeout(i*100);
  }
}

// Use the DOM setInterval() function to change the offset of the symbol
// at fixed intervals.
function animateCircle(line) {
    var count = 0;
    window.setInterval(function() {
      count = (count + 1) % 200;

      var icons = line.get('icons');
      icons[0].offset = (count / 2) + '%';
      line.set('icons', icons);
  }, 20);
}

//DROP Marker	
function dropMarker() {
  marker = new google.maps.Marker({
    map: map,
    draggable: false,
    animation: google.maps.Animation.DROP,
    position: {lat: 59.327, lng: 18.067}
  });
}

//DROP Marker Timeout
function dropMarkerTimeout(timeout){
window.setTimeout(function() {
    markers.push(new google.maps.Marker({
      position: {lat: 59.327, lng: 18.067},
      map: map,
      animation: google.maps.Animation.DROP
    }));
  }, timeout);
}