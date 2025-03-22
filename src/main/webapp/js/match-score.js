const API_URL = '/match-score';

document.addEventListener('DOMContentLoaded', () => {
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

            const container = document.createElement('div')
            for(const key in data) {
                if (data.hasOwnProperty(key)) {
                    const item = document.createElement('p');
                    item.textContent = `${key}: ${data[key]}`;
                    container.appendChild(item);
                }
            }
            document.body.appendChild(container);

            // Здесь можно использовать данные, например, отобразить их на странице
        })
        .catch(error => {
            console.error('Error loading match data:', error);
        });
});