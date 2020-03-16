CREATE TABLE IF NOT EXISTS person (
    id SERIAL PRIMARY KEY,
    registered_name VARCHAR(100),
    doing_business_as VARCHAR(100),
    email VARCHAR(100),
    password VARCHAR(100),
    status VARCHAR(100)
);
