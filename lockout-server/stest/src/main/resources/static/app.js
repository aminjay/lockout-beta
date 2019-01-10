function connect(){
		alert("hey");
		ws = new WebSocket('ws://localhost:8080/name');
		ws.onmessage = function(data){
			showGreeting(data.data);
		}
		setConnected(true);
}

function disconnect() {
	if (ws != null) {
		ws.close();
	}
	setConnected(false);
	console.log("Disconnected");
}

function sendName() {
	ws.send($("#name").val());
}

function showGreeting(message) {
	$("@greetings").append(" " + message + "");
}

