CREATE TABLE IF NOT EXISTS repeater
(
    id SERIAL,
    info VARCHAR(100) NOT NULL,
    official BOOLEAN,
    digital BOOLEAN,
    simplex BOOLEAN,
    echolink BOOLEAN,
    PRIMARY KEY(id)
);