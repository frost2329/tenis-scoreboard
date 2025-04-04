document.addEventListener('DOMContentLoaded', init);

function init() {
    document.getElementById('matchForm').addEventListener('submit', sendNewMatch);
}

async function sendNewMatch(e) {
    e.preventDefault();

    document.getElementById('formError').textContent = '';
    const formData = new FormData();

    formData.append('playerName1', document.getElementById('player1').value.trim());
    formData.append('playerName2', document.getElementById('player2').value.trim());
    try {
        const response = await fetch('/new-match', {
            method: 'POST',
            body: new URLSearchParams(formData),
            redirect: 'follow'
        });
        if (response.redirected) {
            window.location.href = response.url;
        } else if (!response.ok) {
            const data = await response.json();
            document.getElementById('formError').textContent = data.message;
        }

    } catch (error) {
        document.getElementById('formError').textContent = 'An error occurred. Please try again.';
        console.error('Error:', error);
    }
}