var map;
var markers = [];

function initMap() {
    //Style map.
    var customMapType = new google.maps.StyledMapType([{
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
          zoom: 3,
          mapTypeId: [google.maps.MapTypeId.TERRAIN, customMapTypeId] 
    });

    map.mapTypes.set(customMapTypeId, customMapType);
    map.setMapTypeId(customMapTypeId);
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