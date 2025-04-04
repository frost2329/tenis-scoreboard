const API_URL = '/matches-data';
const PAGE_SIZE = 5;
let currentPage = 1;
let matchesData;
let searchTimeout = null;

document.addEventListener('DOMContentLoaded', () => {
    loadMatchesData();

    // Получаем элементы DOM один раз
    const searchInput = document.getElementById('search-input');

    // Обработчики событий
    searchInput.addEventListener('input', handleSearchInput);
    searchInput.addEventListener('keypress', handleSearchKeyPress);
});

function handleSearchInput() {
    clearTimeout(searchTimeout);
    searchTimeout = setTimeout(() => {
        performSearch();
    }, 1000);
}

function handleSearchKeyPress(e) {
    if (e.key === 'Enter') {
        clearTimeout(searchTimeout);
        performSearch();
    }
}

function performSearch() {
    const searchInput = document.getElementById('search-input');
    const searchQuery = searchInput ? (searchInput.value || '').trim() : '';
    loadMatchesData(searchQuery);
}

function loadMatchesData(searchFilter) {
    let url = `${API_URL}?page=${currentPage}&size=${PAGE_SIZE}&filter=${searchFilter||''}`
    fetch(url, {method: 'GET'})
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            matchesData = data.matches;
            showMatchesTable();
            renderPagination(data.totalCount);
        })
        .catch(error => {
            console.error('Ошибка:', error);
        });
}

function showMatchesTable() {
    document.getElementById('loading').style.display = 'none';

    // Показываем таблицу
    const table = document.getElementById('matches-table');
    table.style.display = 'table';

    // Заполняем таблицу данными
    const tbody = document.getElementById('matches-body');
    tbody.innerHTML = '';

    matchesData.forEach(match => {
        const row = document.createElement('tr');

        const player1Cell = document.createElement('td');
        player1Cell.textContent = match.firstPlayer;
        row.appendChild(player1Cell);

        const player2Cell = document.createElement('td');
        player2Cell.textContent = match.secondPlayer;
        row.appendChild(player2Cell);

        const winnerCell = document.createElement('td');
        winnerCell.textContent = match.winner;
        row.appendChild(winnerCell);

        tbody.appendChild(row);
    });
}

function renderPagination(totalMatches) {
    const totalPages = Math.ceil(totalMatches / PAGE_SIZE);
    const paginationContainer = document.querySelector('.pagination-container');
    paginationContainer.innerHTML = '';

    if (totalPages <= 1) return;

    const maxVisiblePages = 3;
    let startPage, endPage;

    if (totalPages <= maxVisiblePages) {
        startPage = 1;
        endPage = totalPages;
    } else {
        startPage = Math.max(1, currentPage - Math.floor(maxVisiblePages / 2));
        endPage = startPage + maxVisiblePages - 1;

        if (endPage > totalPages) {
            endPage = totalPages;
            startPage = endPage - maxVisiblePages + 1;
        }
    }

    if (currentPage > 1) {
        const prevButton = document.createElement('button');
        prevButton.className = 'pag-button';
        prevButton.innerText = '←';
        prevButton.addEventListener('click', () => {
            currentPage--;
            loadMatchesData();
        });
        paginationContainer.appendChild(prevButton);
    }

    if (startPage > 1) {
        const firstButton = document.createElement('button');
        firstButton.className = 'pag-button';
        firstButton.innerText = '1';
        firstButton.addEventListener('click', () => {
            currentPage = 1;
            loadMatchesData();
        });
        paginationContainer.appendChild(firstButton);

    }

    for (let i = startPage; i <= endPage; i++) {
        const pageButton = document.createElement('button');
        pageButton.className = `pag-button ${i === currentPage ? 'active' : ''}`;
        pageButton.innerText = i;
        pageButton.addEventListener('click', () => {
            currentPage = i;
            loadMatchesData();
        });
        paginationContainer.appendChild(pageButton);
    }

    if (endPage < totalPages) {
        const lastButton = document.createElement('button');
        lastButton.className = 'pag-button';
        lastButton.innerText = totalPages;
        lastButton.addEventListener('click', () => {
            currentPage = totalPages;
            loadMatchesData();
        });
        paginationContainer.appendChild(lastButton);
    }

    if (currentPage < totalPages) {
        const nextButton = document.createElement('button');
        nextButton.className = 'pag-button';
        nextButton.innerText = '→';
        nextButton.addEventListener('click', () => {
            currentPage++;
            loadMatchesData();
        });
        paginationContainer.appendChild(nextButton);
    }
}