CREATE SEQUENCE IF NOT EXISTS conversations_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS members_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS messages_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS null_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS users_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE conversations
(
    id           BIGINT NOT NULL,
    message_type VARCHAR(31),
    created      TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_conversations PRIMARY KEY (id)
);

CREATE TABLE members
(
    id              BIGINT NOT NULL,
    alias           VARCHAR(255),
    joined          TIMESTAMP WITHOUT TIME ZONE,
    role            VARCHAR(255),
    user_id         BIGINT NOT NULL,
    conversation_id BIGINT NOT NULL,
    CONSTRAINT pk_members PRIMARY KEY (id)
);

CREATE TABLE messages
(
    id              BIGINT NOT NULL,
    message_type    VARCHAR(31),
    conversation_id BIGINT NOT NULL,
    author_id       BIGINT NOT NULL,
    timestamp       TIMESTAMP WITHOUT TIME ZONE,
    photo_name VARCHAR(255),
    text_content TEXT,
    file_name VARCHAR(255),
    CONSTRAINT pk_messages PRIMARY KEY (id)
);

CREATE TABLE users
(
    id       BIGINT       NOT NULL,
    email    VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    name     VARCHAR(255) NOT NULL,
    avatar   VARCHAR(255),
    status   VARCHAR(255),
    created  TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE members
    ADD CONSTRAINT FK_MEMBERS_ON_CONVERSATION FOREIGN KEY (conversation_id) REFERENCES conversations (id);

ALTER TABLE members
    ADD CONSTRAINT FK_MEMBERS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE messages
    ADD CONSTRAINT FK_MESSAGES_ON_AUTHOR FOREIGN KEY (author_id) REFERENCES members (id);

ALTER TABLE messages
    ADD CONSTRAINT FK_MESSAGES_ON_CONVERSATION FOREIGN KEY (conversation_id) REFERENCES conversations (id);