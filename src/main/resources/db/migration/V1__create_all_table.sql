CREATE SEQUENCE IF NOT EXISTS conversations_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS file_messages_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS members_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS messages_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS null_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS photo_messages_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS text_messages_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS users_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE conversation_members
(
    conversation_id BIGINT NOT NULL,
    member_id       BIGINT NOT NULL
);

CREATE TABLE conversations
(
    id           BIGINT NOT NULL,
    message_type VARCHAR(31),
    created      TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_conversations PRIMARY KEY (id)
);

CREATE TABLE file_messages
(
    id        BIGINT NOT NULL,
    file_name VARCHAR(255),
    CONSTRAINT pk_file_messages PRIMARY KEY (id)
);

CREATE TABLE members
(
    id      BIGINT NOT NULL,
    alias   VARCHAR(255),
    joined  TIMESTAMP WITHOUT TIME ZONE,
    role    VARCHAR(255),
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_members PRIMARY KEY (id)
);

CREATE TABLE messages
(
    id              BIGINT NOT NULL,
    message_type    VARCHAR(31),
    conversation_id BIGINT NOT NULL,
    author_id       BIGINT NOT NULL,
    timestamp       TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_messages PRIMARY KEY (id)
);

CREATE TABLE photo_messages
(
    id         BIGINT NOT NULL,
    photo_name VARCHAR(255),
    CONSTRAINT pk_photo_messages PRIMARY KEY (id)
);

CREATE TABLE text_messages
(
    id BIGINT NOT NULL,
    text_content TEXT,
    CONSTRAINT pk_text_messages PRIMARY KEY (id)
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

ALTER TABLE file_messages
    ADD CONSTRAINT FK_FILE_MESSAGES_ON_ID FOREIGN KEY (id) REFERENCES messages (id);

ALTER TABLE members
    ADD CONSTRAINT FK_MEMBERS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE messages
    ADD CONSTRAINT FK_MESSAGES_ON_AUTHOR FOREIGN KEY (author_id) REFERENCES members (id);

ALTER TABLE messages
    ADD CONSTRAINT FK_MESSAGES_ON_CONVERSATION FOREIGN KEY (conversation_id) REFERENCES conversations (id);

ALTER TABLE photo_messages
    ADD CONSTRAINT FK_PHOTO_MESSAGES_ON_ID FOREIGN KEY (id) REFERENCES messages (id);

ALTER TABLE text_messages
    ADD CONSTRAINT FK_TEXT_MESSAGES_ON_ID FOREIGN KEY (id) REFERENCES messages (id);

ALTER TABLE conversation_members
    ADD CONSTRAINT fk_conmem_on_conversation FOREIGN KEY (conversation_id) REFERENCES conversations (id);

ALTER TABLE conversation_members
    ADD CONSTRAINT fk_conmem_on_member FOREIGN KEY (member_id) REFERENCES members (id);