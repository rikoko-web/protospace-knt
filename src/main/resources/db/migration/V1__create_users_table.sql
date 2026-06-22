CREATE TABLE users (
    id SERIAL NOT NULL,
    name VARCHAR(256) NOT NULL,
    email VARCHAR(256) NOT NULL UNIQUE,
    password VARCHAR(256) NOT NULL,
    profile TEXT NOT NULL,         -- 💡新しく追加：プロフィール用
    occupation VARCHAR(256) NOT NULL, -- 💡新しく追加：所属（仕事）用
    position VARCHAR(256) NOT NULL,   -- 💡新しく追加：役職用
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);