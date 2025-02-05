create table metadata
(
    id       INTEGER NOT NULL PRIMARY KEY,
    name     VARCHAR(100),
    artist   VARCHAR(100),
    album    VARCHAR(100),
    duration VARCHAR(100),
    year     INT CHECK (year BETWEEN 1900 AND 2099)
);

