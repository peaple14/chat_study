function createOrJoinRoom() {
    const nickname = document.getElementById('nickname').value;
    const roomName = document.getElementById('roomName').value;

    if (!nickname || !roomName) {
        alert('닉네임과 방 이름을 입력해주세요.');
        return;
    }

    localStorage.setItem('nickname', nickname);
    localStorage.setItem('roomName', roomName);
    
    window.location.href = 'chat.html';
}
