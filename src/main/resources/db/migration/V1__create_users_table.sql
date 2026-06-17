CREATE TABLE prototypes (
    id SERIAL NOT NULL,
    title VARCHAR(256) NOT NULL,
    catch_copy VARCHAR(512),
    concept TEXT,
    image VARCHAR(256),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (id)
);