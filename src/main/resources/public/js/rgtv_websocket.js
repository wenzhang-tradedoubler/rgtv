
var sock = new SockJS('/rgtv/messages');

sock.onopen = function () {
    console.log("Opened websocket connection to rgtv server!");
};

sock.onmessage = function (e) {
    var data = JSON.parse(e.data);
    console.log("Got data from rgtv server" + data);
    var type = data.type;
    if(type == 0){
        //Handle clicks/trackbacks
        var position = new google.maps.LatLng(data.lat, data.lng);
        var eventType = data.event;
        dropMarkerTimeout(position, 200, eventType == 0);
    }else {
        console.log("TODO: handle statistics");
    }
};

sock.onclose = function () {
    console.log("Closed websocket connection to rgtv server!");
};