# java-filmorate
Template repository for Filmorate project.

```mermaid
erDiagram

    USERS {
        bigint user_id PK, FK
        text email
        text login
        text name
        timestamp birthday
    }

    USERS ||--o{ LIKES: make
    FILMS ||--o{ LIKES: has
    LIKES {
        bigint film_id PK, FK
        bigint user_id PK, FK
    }

    USERS ||--o{ FRIENDS: has
    FRIENDS {
        bigint user_id PK, FK
        bigint friend_id PK, FK
        bigint status_id FK
    }

    FILMS {
        bigint film_id PK, FK
        text name
        text description
        timestamp releaseDate
        int duration
        bigint genre_id FK
        bigint rate_id FK
    }

    FILMS ||--o{ GENRES: contains
    GENRES {
        bigint genre_id PK, FK
        text name
    }

    FILMS ||--|| RATES: contains
    RATES {
        bigint rate_id PK, FK
        text name
    }

    FRIENDS ||--|| STATUSES: has
    STATUSES {
        bigint status_id PK, FK
        text name
    }
```