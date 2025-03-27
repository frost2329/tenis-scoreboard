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
            matchData = data;
            updateScoreboard();
        })
        .catch(error => {
            console.error('Ошибка:', error);
        });
}

function sendScorePoint(event) {
    const buttonId = event.target.id;
    const data = {
        uuid:  matchData.uuid,
        playerId: buttonId === 'score-button-1' ? matchData.firstPlayerId : matchData.secondPlayerId
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
            matchData = data;
            updateScoreboard();
            if(matchData.finished) {
                disableButtons();
                showWinner();
            }
        })
        .catch(error => {
            console.error('Ошибка:', error);
        });
}

function updateScoreboard() {
    // Обновляем имя первого игрока
    document.querySelector('#player-name-1').textContent = matchData.firstPlayerName;

    // Обновляем счет первого игрока
    const firstPlayerScores = document.querySelectorAll('#player-score-1:nth-child(2) .score');
    firstPlayerScores[0].textContent = matchData.firstPlayerScore.sets; // Sets
    firstPlayerScores[1].textContent = matchData.firstPlayerScore.games; // Games
    firstPlayerScores[2].textContent = matchData.firstPlayerScore.points; // Points

    // Обновляем имя второго игрока
    document.querySelector('#player-name-2').textContent = matchData.secondPlayerName;

    // Обновляем счет второго игрока
    const secondPlayerScores = document.querySelectorAll('#player-score-2:nth-child(2) .score');
    secondPlayerScores[0].textContent = matchData.secondPlayerScore.sets; // Sets
    secondPlayerScores[1].textContent = matchData.secondPlayerScore.games; // Games
    secondPlayerScores[2].textContent = matchData.secondPlayerScore.points; // Points
}
function disableButtons() {
    let buttonScore1 = document.querySelector('#score-button-1');
    let buttonScore2 = document.querySelector('#score-button-2');
    buttonScore1.disabled = true;
    buttonScore2.disabled = true;
}

function showWinner() {
    let scoreboard = document.querySelector('.scoreboard');
    let winnerElem = document.createElement("div");
    winnerElem.append("Игра завершена")
    scoreboard.append(winnerElem);
}

