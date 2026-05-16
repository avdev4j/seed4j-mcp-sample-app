CREATE TABLE breed (
    id   UUID         PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE pet (
    id       UUID         PRIMARY KEY,
    name     VARCHAR(100) NOT NULL,
    breed_id UUID         NOT NULL REFERENCES breed (id)
);

CREATE INDEX idx_pet_breed_id ON pet (breed_id);

INSERT INTO breed (id, name) VALUES
    ('11111111-1111-1111-1111-111111111111', 'cane corso');

INSERT INTO pet (id, name, breed_id) VALUES
    ('22222222-2222-2222-2222-222222222222', 'umee', '11111111-1111-1111-1111-111111111111');
