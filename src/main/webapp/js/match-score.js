const API_URL = '/match-score';
let matchData;

document.addEventListener('DOMContentLoaded', init);
document.addEventListener('DOMContentLoaded', loadScoreData);

function init() {
    const scoreButtons = document.querySelectorAll('.button-score button');
    scoreButtons.forEach(button => {
        button.addEventListener('click', sendScorePoint)
    })
}

function loadScoreData() {
    const urlParams = new URLSearchParams(window.location.search);
    const uuid = urlParams.get('uuid');

    fetch(`${API_URL}?uuid=${uuid}`, {
        method: 'GET'
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json(); // Парсим JSON-ответ
        })
        .then(data => {
            console.log('Match data:', data);
            matchData = data;
            updateScoreboard(data);
        })
        .catch(error => {
            console.error('Ошибка:', error);
        });
}

function sendScorePoint(event) {
    const playerName = event.target.closest('.player-name');
    const data = {
        uuid:  matchData.uuid,
        playerId: 1
    };

    fetch(API_URL, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json(); // Парсим JSON-ответ
        })
        .then(data => {
            console.log('Успешно:', data);
            matchData = data;
        })
        .catch(error => {
            console.error('Ошибка:', error);
        });
}

function updateScoreboard(data) {
    // Обновляем имя первого игрока
    document.querySelector('#player-name-1').textContent = data.firstPlayerName;

    // Обновляем счет первого игрока
    const firstPlayerScores = document.querySelectorAll('#player-score-1:nth-child(2) .score');
    firstPlayerScores[0].textContent = data.firstPlayerScore.sets; // Sets
    firstPlayerScores[1].textContent = data.firstPlayerScore.games; // Games
    firstPlayerScores[2].textContent = data.firstPlayerScore.points; // Points

    // Обновляем имя второго игрока
    document.querySelector('#player-name-2').textContent = data.secondPlayerName;

    // Обновляем счет второго игрока
    const secondPlayerScores = document.querySelectorAll('#player-score-2:nth-child(2) .score');
    secondPlayerScores[0].textContent = data.secondPlayerScore.sets; // Sets
    secondPlayerScores[1].textContent = data.secondPlayerScore.games; // Games
    secondPlayerScores[2].textContent = data.secondPlayerScore.points; // Points
}

