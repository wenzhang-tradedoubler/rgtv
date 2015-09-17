
var sock = new SockJS('/rgtv/messages');

sock.onopen = function () {
    console.log("Opened websocket connection to rgtv server!");
};

sock.onmessage = function (e) {
    console.log("Got data from rgtv server" + e.data);
    var position = new google.maps.LatLng(JSON.parse(e.data).lat, JSON.parse(e.data).lng);
    dropMarkerTimeout(position, 200, true);
};

sock.onclose = function () {
    console.log("Closed websocket connection to rgtv server!");
};