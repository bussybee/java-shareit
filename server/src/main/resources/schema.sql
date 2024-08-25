DROP TABLE IF EXISTS users, items, requests, bookings CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name  VARCHAR(150)        NOT NULL,
    email VARCHAR(320) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS requests
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    description  VARCHAR(400)                NOT NULL,
    requester_id BIGINT REFERENCES users (id),
    created_time TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS items
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        VARCHAR(150) UNIQUE NOT NULL,
    description VARCHAR(400)        NOT NULL,
    available   BOOLEAN             NOT NULL,
    owner_id    BIGINT REFERENCES users (id),
    request_id  BIGINT REFERENCES requests (id)
);

CREATE TABLE IF NOT EXISTS bookings
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_date   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    item_id    BIGINT REFERENCES items (id),
    booker_id  BIGINT REFERENCES users (id),
    status     VARCHAR                     NOT NULL
);

CREATE TABLE IF NOT EXISTS comments
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    text      VARCHAR(400)                NOT NULL,
    item_id   BIGINT REFERENCES items (id),
    author_id BIGINT REFERENCES users (id),
    created   TIMESTAMP WITHOUT TIME ZONE NOT NULL
);
