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
                return response.json()
                    .then(errorData => {
                        throw new Error(errorData.message || `HTTP error! Status: ${response.status}`);
                    });
            }
            return response.json();
        })
        .then(data => {
            matchData = data;
            updateScoreboard();
            if(matchData.isFinished) {
                disableButtons();
                showWinner();
            }
        })
        .catch(error => {
            showError(error);
            console.error('Ошибка:', error);
        });
}

function sendScorePoint(event) {
    const buttonId = event.target.id;
    const data = {
        uuid:  matchData.uuid,
        playerId: buttonId === 'score-button-1' ? matchData.firstPlayer.playerId : matchData.secondPlayer.playerId
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
            if(matchData.isFinished) {
                disableButtons();
                showWinner();
            }
        })
        .catch(error => {
            console.error('Ошибка:', error);
        });
}

function updateScoreboard() {
    const playerLine1 = document.querySelector('#player-line-1');
    const playerLine2 = document.querySelector('#player-line-2');
    updatePlayerLine(playerLine1, matchData.firstPlayer);
    updatePlayerLine(playerLine2, matchData.secondPlayer);
}

function updatePlayerLine(playerLine, player) {
    const playerNameElement = playerLine.querySelector('.player-name');
    playerNameElement.textContent = player.playerName;
    const playerScoreDiv = playerLine.querySelector('.player-score');
    const scoreElements = playerScoreDiv.querySelectorAll('.score');
    scoreElements[0].textContent = player.sets;
    scoreElements[1].textContent = player.games;
    scoreElements[2].textContent = player.points;
}

function disableButtons() {
    let buttonScore1 = document.querySelector('#score-button-1');
    let buttonScore2 = document.querySelector('#score-button-2');
    buttonScore1.disabled = true;
    buttonScore2.disabled = true;
}

function showWinner() {
    const winnerBlock = document.querySelector('.winner-block');
    winnerBlock.style.display = 'flex';
    const winnerName = matchData.firstPlayerScore.sets === 2 ? matchData.firstPlayerName : matchData.secondPlayerName;
    winnerBlock.textContent = "Игра завершена. " + winnerName + " победил!";
}

function showError(errorMessage) {
    const errorBlock = document.querySelector('.error-block');
    errorBlock.style.display = 'flex';
    errorBlock.textContent = errorMessage;
}

