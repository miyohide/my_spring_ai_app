// チャットのメッセージを表示する領域
const chatMessages = document.getElementById('chat-messages');
// メッセージ送信用のフォーム
const chatForm = document.getElementById('chat-form');
// メッセージの入力領域
const messageInput = document.getElementById('message-input');

// フォームが送信された時の処理
chatForm.addEventListener('submit', async (e) => {
    // デフォルトの挙動をキャンセル
    e.preventDefault();
    // 入力された値を取得し、次の入力のために空にする
    const message = messageInput.value;
    messageInput.value = '';

    // 入力したメッセージを出力する
    chatMessages.innerHTML += `<div><strong>You:</strong> ${message}</div>`;

    // AIが返した値を出力する領域を作成する
    const messageDiv = document.createElement('div');
    messageDiv.innerHTML = '<strong>AI:</strong>';
    const aiContents = document.createElement('span');
    messageDiv.appendChild(aiContents);
    chatMessages.appendChild(messageDiv);

    try {
        const response = await fetchStreamWithRetry(`/chatstream?message=${encodeURIComponent(message)}`);
        // ReadableStreamオブジェクトを取得（response.body）し、ストリームのデータを受け取るreaderを取得
        const reader = response.body.getReader();
        // readerから受け取ったバイナリデータをデコードするための準備
        const decoder = new TextDecoder();

        while (true) {
            const { value, done } = await reader.read();
            if (done) {
                break;
            }
            // stream: trueを指定して前のチャンクとの文脈を保持しながらデコード
            // decodedChunkは1回の読み取りで得られたテキストの断片が格納される
            const decodedChunk = decoder.decode(value, { stream: true });
            aiContents.textContent += decodedChunk;
        }
    } catch (error) {
        console.error('Error:', error);
        aiContents.textContent += 'Error occured while fetching the response.';
    }
    // 常に最新のメッセージが見れるようにスクロール
    chatMessages.scrollTop = chatMessages.scrollHeight;
});

/**
 * 指定されたURLにストリーミングリクエストを送信し、失敗時は再試行を行う
 * @param {string} url - リクエスト先のURL
 * @param {number} retries - 再試行回数(デフォルト値: 3)
 * @returns {Promise<Response>} - フェッチレスポンス
 * @throws {Error} - 全ての再試行が失敗した場合にエラーを投げる
 */
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
