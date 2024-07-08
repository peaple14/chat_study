let ws;
let currentChatRoom = "";

function connect(chatRoomId) {
    currentChatRoom = chatRoomId;
    ws = new WebSocket("ws://localhost:8080/chat");

    ws.onopen = function() {
        console.log("Connected to chat room: " + chatRoomId);
    };

    ws.onmessage = function(event) {
        console.log("Message received: " + event.data);
        const messages = document.getElementById('messages');
        const message = document.createElement('li');
        message.appendChild(document.createTextNode(event.data));
        messages.appendChild(message);
    };

    ws.onclose = function() {
        console.log("Disconnected from chat room: " + chatRoomId);
    };

    ws.onerror = function(error) {
        console.error("WebSocket error: ", error);
    };
}

function sendMessage() {
    const input = document.getElementById('messageInput');
    const message = currentChatRoom + ":" + input.value;
    console.log("Sending message: " + message);
    ws.send(message);
    input.value = '';
}

function joinRoom() {
    const roomInput = document.getElementById('roomInput');
    connect(roomInput.value);
}
