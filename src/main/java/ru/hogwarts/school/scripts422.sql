CREATE TABLE People
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    age         INTEGER      NOT NULL,
    has_license BOOLEAN      NOT NULL
);

CREATE TABLE Cars
(
    id    SERIAL PRIMARY KEY,
    brand VARCHAR(50)    NOT NULL,
    model VARCHAR(50)    NOT NULL,
    price NUMERIC(10, 2) NOT NULL
);

CREATE TABLE PeopleCars
(
    person_id INTEGER REFERENCES People (id) ON DELETE CASCADE,
    car_id    INTEGER REFERENCES Cars (id) ON DELETE CASCADE,
    PRIMARY KEY (person_id, car_id)
);