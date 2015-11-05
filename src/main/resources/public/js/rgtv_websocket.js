var sockGeoLocation = new SockJS('/rgtv/stomp');
var sockNumberStatistics = new SockJS('/rgtv/stomp');

sockGeoLocation.onopen = function () {
    console.log("Opened websocket connection to rgtv server!");
};
sockNumberStatistics.onopen = function () {
    console.log("Opened websocket connection to rgtv server!");
};

var stompGeoLocationClient = Stomp.over(sockGeoLocation);

stompGeoLocationClient.connect({ }, function(frame) {
    // subscribe to the /topic/geolocation endpoint
    stompGeoLocationClient.subscribe("/topic/geolocation", function(data) {
        var data = JSON.parse(data.body);
        var position = new google.maps.LatLng(data.lat, data.lng);
        var eventType = data.event;
        dropMarkerTimeout(position, 200, eventType == 0);
    });
});

var stompNumberStatisticsClient = Stomp.over(sockNumberStatistics);
stompNumberStatisticsClient.connect({ }, function(frame) {
    // subscribe to the /topic/numberstatistics endpoint
    stompNumberStatisticsClient.subscribe("/topic/numberstatistics", function(data) {
        var data = JSON.parse(data.body);
        var numClicks = data.click;
        var numTrackbacks = data.trackback;
        updateStatistics(numClicks, numTrackbacks);
    });
});


sockGeoLocation.onclose = function () {
    console.log("Closed websocket connection to rgtv server!");
};
sockNumberStatistics.onclose = function () {
    console.log("Closed websocket connection to rgtv server!");
};