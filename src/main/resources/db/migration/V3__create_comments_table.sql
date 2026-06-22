CREATE TABLE comments (
    id SERIAL PRIMARY KEY,
    comment_text TEXT NOT NULL,
    user_id BIGINT NOT NULL,
    prototype_id BIGINT NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (prototype_id) REFERENCES prototypes(id)
);