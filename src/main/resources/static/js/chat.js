const userInput = document.getElementById('message');
const chatForm = document.getElementById('chat-form');
const chatHistory = document.getElementById('chat-history');

chatForm.addEventListener('submit', async (e) => {
    // デフォルトの挙動をキャンセル
    e.preventDefault();

    // 入力された値を取得し、テキストボックスは次の入力のために空にする
    const message = userInput.value;
    userInput.value = '';
    // 入力された値を画面表示
    chatHistory.innerHTML += `<div><strong>You: ${message}</strong></div>`;

    // AIが返した値を出力する領域を作成する
    // 以下のような構成にする
    // <div id="chat-history">  <-- chatHistory
    //   <div><strong>You:</strong> Hello</div>
    //   <div>  <-- messageDiv
    //     <strong>AI:</strong>
    //       <span>  <-- aiContents
    //         Hi there!
    //       </span>
    //   </div>
    // </div>
    const messageDiv = document.createElement('div');
    messageDiv.innerHTML = '<strong>AI:</strong>';
    const aiContents = document.createElement('span');
    messageDiv.appendChild(aiContents);
    chatHistory.appendChild(messageDiv);

    // Bedrockにリクエスト送信
    try {
      const response = await fetch(`/chat?message=${encodeURIComponent(message)}`, {
        method: 'POST',
      });
      const jsonData = await response.json();
      // JSONからテキスト部分を抽出（Bedrockのレスポンス構造に応じて調整）
      const assistantText = jsonData.response;
      aiContents.textContent = assistantText;
    } catch (error) {
      console.error('Error:', error);
      chatHistory.innerHTML += `<div class="error-message">エラーが発生しました</div>`;
    };
  });
