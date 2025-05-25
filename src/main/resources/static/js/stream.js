const chatMessages = document.getElementById('chat-messages');
const chatForm = document.getElementById('chat-form');
const messageInput = document.getElementById('message-input');

chatForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    const message = messageInput.value;
    messageInput.value = '';

    chatMessages.innerHTML += `<div><strong>You:</strong> ${message}</div>`;

    const messageDiv = document.createElement('div');
    messageDiv.innerHTML = '<strong>AI:</strong>';
    const aiContents = document.createElement('span');
    messageDiv.appendChild(aiContents);
    chatMessages.appendChild(messageDiv);

    try {
        const response = await fetchStreamWithRetry(`/chatstream?message=${encodeURIComponent(message)}`);
        const reader = response.body.getReader();
        const decoder = new TextDecoder();

        while (true) {
            const { value, done } = await reader.read();
            if (done) {
                break;
            }
            const decodedChunk = decoder.decode(value, { stream: true });
            aiContents.textContent += decodedChunk;
        }
    } catch (error) {
        console.error('Error:', error);
        aiContents.textContent += 'Error occured while fetching the response.';
    }
    chatMessages.scrollTop = chatMessages.scrollHeight;
});

async function fetchStreamWithRetry(url, retries = 3) {
    for (let i = 0; i < retries; i++) {
        try {
            const response = await fetch(url);
            if (response.ok) {
                return response;
            }
        } catch (error) {
            if (i === retries -1) {
                throw error;
            }
        }
    }
}
