let ws;
const nickname = localStorage.getItem('nickname') || 'guest';
const roomName = localStorage.getItem('roomName') || 'defaultRoom';

function connect() {
    ws = new WebSocket(`ws://34.16.226.33:8080/chat`);
    ws.onopen = function() {
        console.log("Connected to WebSocket server");
        document.getElementById('status').innerText = 'Connected';
        const joinMessage = {
            author: nickname,
            message: `${nickname} joined the room.`,
            time: new Date().toISOString()
        };
        ws.send(JSON.stringify(joinMessage));
        document.getElementById('currentRoomName').innerText = roomName;
    };

    ws.onmessage = function(event) {
        const msg = JSON.parse(event.data);
        console.log("Message received: ", msg);

        if (msg.author && msg.message && msg.time) {
            addMessageToDOM(msg.author, msg.message, msg.time);
        } else {
            console.error("Invalid message format: ", msg);
        }
    };

    ws.onclose = function() {
        console.log("Disconnected from WebSocket server");
        document.getElementById('status').innerText = 'Disconnected';
        setTimeout(connect, 3000);
    };

    ws.onerror = function(error) {
        console.error("WebSocket error: ", error);
    };
}

function sendMessage() {
    if (!ws || ws.readyState !== WebSocket.OPEN) {
        console.error("WebSocket is not open. Cannot send message.");
        return;
    }

    const input = document.getElementById('messageInput');
    const message = {
        author: nickname,
        message: input.value,
        time: new Date().toISOString()
    };

    ws.send(JSON.stringify(message));
    input.value = '';
}

function addMessageToDOM(author, message, time) {
    const messages = document.getElementById('messages');
    const messageElement = document.createElement('li');
    messageElement.appendChild(document.createTextNode(`${author}: ${message} [${time}]`));
    messages.appendChild(messageElement);
    messages.scrollTop = messages.scrollHeight; // 새로운 메시지가 추가될 때 스크롤을 아래로 이동
}

function handleKeyPress(event) {
    if (event.key === 'Enter') {
        sendMessage();
    }
}

document.addEventListener('DOMContentLoaded', (event) => {
    document.getElementById('messageInput').addEventListener('keypress', handleKeyPress);
    connect();
});
