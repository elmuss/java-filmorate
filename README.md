# java-filmorate
Template repository for Filmorate project.

```mermaid
erDiagram

    USERS {
        bigint user_id PK
        text email
        text login
        text name
        timestamp birthday
    }

    USERS ||--o{ LIKES: make
    FILMS ||--o{ LIKES: has
    LIKES {
        bigint film_id PK
        bigint user_id PK
    }

    USERS ||--o{ FRIENDS: has
    FRIENDS {
        bigint user_id PK
        bigint friend_id PK
    }

    FILMS {
        bigint film_id PK
        text name
        text description
        timestamp release_date
        int duration
        bigint film_mpa
    }

    FILMS }o--o{ FILMS_GENRES: connects
    FILMS_GENRES {
        bigint film_id PK
        bigint genre_id PK
    }

    FILMS_GENRES ||--o{ GENRES: contains
    GENRES {
        bigint id PK
        text name
    }

    FILMS ||--|| RATES: contains
    RATES {
        bigint id PK
        text name
    }
    
```
## Примеры запросов для базы данных:
#### **1. Просмотр всех фильмов**
```
SELECT id,
       name,
       description,
       releaseDate,
       duration
FROM films;
```
#### **2. Просмотр всех пользователей**
```
SELECT id,
       name,
       login,
       email,
       birthday
FROM users;
```