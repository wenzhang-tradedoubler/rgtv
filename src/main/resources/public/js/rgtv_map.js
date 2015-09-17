var map;
var markers = [];

function initMap() {
    //Style map.
    var rgtvMapTypeId = 'rgtvMapTypeId';
    var rgtvMapType = new google.maps.StyledMapType([{
        stylers: [{
            gamma: 0.68
        }, {
            invert_lightness: false
        }, {
            lightness: -50
        }, {
            saturation: -60
        }]
    }, {
        elementType: 'landscape',
        stylers: [{
            visibility: 'on'
        }, {
            lightness: -50
        }]
    }, {
        featureType: 'poi',
        stylers: [{
            "visibility": "off"
        }]
    }, {
        featureType: 'water',
        stylers: [{
            color: '#31373E'
        }, {
            lightness: -30
        }]
    }], {
        name: 'Custom Style'
    });
    var customMapTypeId = 'custom_style';

    //Create map and set center to Tradedoubler.
     map = new google.maps.Map(document.getElementById('map'), {  center: {
            lat: 59.341112,
            lng: 18.064447
        },
          zoom: 2,
          mapTypeId: [google.maps.MapTypeId.TERRAIN, rgtvMapTypeId] 
    });

    map.mapTypes.set(rgtvMapTypeId, rgtvMapType);
    map.setMapTypeId(rgtvMapTypeId);
}

//Async drop marker
function dropMarkerTimeout(markerPosition, timeout, isClick) {
    window.setTimeout(function () {
        markers.push(new google.maps.Marker({
            position: markerPosition,
            map: map,
            animation: google.maps.Animation.DROP,
            icon: isClick ? "images/click.png" : "images/trackback.png"
        }));
    }, timeout);
}

//Show cycles instead of marker
//function showCycleTimeout(cyclePos, timeout, isClick){
//      var newCircle = new google.maps.Circle({
//        radius: 50,
//        strokeColor: '#FF0000',
//        strokeOpacity: 0.8,
//        strokeWeight: 2,
//        fillColor: isClick ? '#96F70D' : '#D21F1F',
//        fillOpacity: 0.35,
//        center: cyclePos,
//        map: map
//      });
//}