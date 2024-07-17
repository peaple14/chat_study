document.addEventListener('DOMContentLoaded', (event) => {
    const nickname = localStorage.getItem('nickname') || 'guest';
    const pathArray = window.location.pathname.split('/');
    const roomName = pathArray[pathArray.length - 1] || 'defaultRoom';
    let ws;
    let reconnectAttempts = 0;
    const maxReconnectAttempts = 5;

    function getWebSocketURL(roomName) {
        const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
        const host = window.location.host;
        return `${protocol}//${host}/ws/chat/${roomName}`;
    }

    function connect() {
        if (reconnectAttempts >= maxReconnectAttempts) {
            console.error("Max reconnect attempts reached.");
            return;
        }
        ws = new WebSocket(getWebSocketURL(roomName));

        ws.onopen = function() {
            reconnectAttempts = 0;
            console.log("Connected to WebSocket server");
            document.getElementById('status').innerText = 'Connected';
            const joinMessage = {
                author: 'Server',
                message: `${nickname} joined the room.`,
                time: new Date().toISOString()
            };
            ws.send(JSON.stringify(joinMessage));
            document.getElementById('currentRoomName').innerText = roomName;
            fetchUserList();
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
            if (reconnectAttempts < maxReconnectAttempts) {
                setTimeout(connect, 3000);
                reconnectAttempts++;
            }
        };

        ws.onerror = function(error) {
            console.error("WebSocket error: ", error);
        };
    }

    function fetchUserList() {
        fetch(`/room_list/${roomName}/users`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                const usersElement = document.getElementById('users');
                usersElement.innerHTML = '';
                data.forEach(user => {
                    const li = document.createElement('li');
                    li.textContent = user;
                    usersElement.appendChild(li);
                });
            })
            .catch(error => {
                console.error('Error fetching user list:', error);
            });
    }

    function addMessageToDOM(author, message, time) {
        const messages = document.getElementById('messages');
        const messageElement = document.createElement('li');
        const date = new Date(time);
        const formattedTime = date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });

        messageElement.textContent = `${author}: ${message} [${formattedTime}]`;
        messageElement.classList.add('message');
        if (author === 'Server') {
            messageElement.classList.add('server');
        } else if (author === nickname) {
            messageElement.classList.add('author');
        }
        messages.appendChild(messageElement);
        messages.scrollTop = messages.scrollHeight;
    }

    document.getElementById('messageInput').addEventListener('keypress', function(event) {
        if (event.key === 'Enter') {
            sendMessage();
        }
    });

    document.querySelector('.message-input button').addEventListener('click', function(event) {
        sendMessage();
    });

    function sendMessage() {
        const input = document.getElementById('messageInput');
        const message = input.value.trim();

        if (!message || message.length > 500) {
            alert('메시지는 500자 이하로 입력해주세요.');
            return;
        }

        const messageObject = {
            author: nickname,
            message: escapeHtml(message),
            time: new Date().toISOString()
        };

        ws.send(JSON.stringify(messageObject));
        input.value = '';
        input.focus();
    }

    function escapeHtml(unsafe) {
        return unsafe
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;");
    }

    document.querySelector('.message-input button').addEventListener('click', sendMessage);

    connect();
});
