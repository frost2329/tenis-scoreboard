# Теннисный матч: онлайн-табло

Это веб-приложение позволяет создавать, проводить и сохранять результаты теннисных матчей. Оно построено по архитектуре MVCS (Model-View-Controller-Service) и использует:  
- **H2** – in-memory база данных для хранения информации.  
- **Hibernate** – ORM-фреймворк для работы с данными.  
- **HTML + CSS + JS** – для отображения интерфейса.   
- **JUnit 5** – для модульного тестирования.
- Ссылка на [деплой](http://185.106.94.66:8080/)

Приложение включает валидацию пользовательских запросов и обработку ошибок с удобным отображением.  

## Функционал
- Создание нового матча
- Управление счетом матча в реальном времени
- Сохранение завершенного матча
- Просмотр завершенных матчей
- Поиск по имени игрока

## Уровень представления
- index.html - Стартовая страница, с возможностью начать новый матч или посмотреть завершенные
- new-match.html - Страница старта нового матча. Для старта необходимо ввести имя 2 пользователей
- match-score.html - Страница для управления матчем в реальном времени. Когда матч завершается кнопки добавления очков блокируются, и выводится сообщение о завершении матча
- matches.html - Страница завершенных матчей. Реализована пагинация и поиск по игроку

## Сервлеты
#### NewMatchServlet ("/new-match")
- GET отдает страницу new-match.html
- POST принимает параметры playerName1 и playerName2, создает новый матч, редиректит на /match-table?uuid=" + uuid

#### MatchTableServlet ("/match-table")
- GET редиректит на страницу match-score.html

#### MatchScoreServlet ("/match-score")
- GET Принимает параметр uuid матча, отдает модель состояния текущего матча
- POST принимает JSON в формате {uuid: uuid, playerId:playerId}, обновляет счет матча, отдает модель состояния текущего матча

#### FinishedMatchesServlet ("/matches")
- GET редиректит на страницу matches.html

#### FinishedMatchesDataServlet ("/matches-data")
- GET принимает 3 параметра (page, size, filter), в соответствии с параметрами отдает список завершенных матчей

## Сервисы
- PlayerService - по запросу возвращает существующего или создает нового игрока
- OngoingMatchService - хранит в себе список текущих матчей, предоставляет возможность создать текущий матч, удалить и получить модель текущего матча
- MatchScoreCalculatorService - хранит в себе бизнес-логику подсчета очков матча, дает возможность обновить счет у матча, отдает модель матча после обновления счета
- FinishedMatchService - предоставляет возможность сохранить завершенный матч в БД и получить из БД список завершенных матчей
- ValidatorService - предоставляет возможность провалидировать входящие запросы, выбрасывает соответствующий Exception если запрос некорректный

## Репозитории
- PlayerRepository - сохранение игрока, поиск по ID, поиск по имени
- MatchRepository - сохранение завершенного матча, получение списка заврешенных матчей по фильтрам

## Тесты
Для реализации тестов ипользован JUnit5. Тестами покрыты взаимодействия с БД на уровне репозиториев и логика подсчета очков в матче, подсчет очков при тай брейке

## Цель проекта
- Проект выполнен в рамках [Roadmap Сергея Жукова](https://zhukovsd.github.io/java-backend-learning-course/)
- ТЗ к проекту [ТЗ](https://zhukovsd.github.io/java-backend-learning-course/projects/tennis-scoreboard/)

 
