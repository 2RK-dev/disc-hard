-- Users table
CREATE TABLE users
(
    id       UUID         NOT NULL,
    email    VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    name     VARCHAR(255) NOT NULL,
    avatar   VARCHAR(255),
    status   VARCHAR(255),
    created  TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uc_users_email UNIQUE (email)
);

-- Conversations table with proper discriminator for inheritance
CREATE TABLE conversations
(
    id           UUID NOT NULL,
    message_type VARCHAR(31),  -- Renamed from dtype to message_type for consistency
    created      TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_conversations PRIMARY KEY (id)
);

-- Members table with association to conversations
CREATE TABLE members
(
    id      UUID NOT NULL,
    alias   VARCHAR(255),
    joined  TIMESTAMP WITHOUT TIME ZONE,
    role    VARCHAR(255),
    user_id UUID NOT NULL,
    CONSTRAINT pk_members PRIMARY KEY (id),
    CONSTRAINT FK_MEMBERS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id)
);

-- Association table between members and conversations (many-to-many)
CREATE TABLE conversation_members
(
    conversation_id UUID NOT NULL,
    member_id       UUID NOT NULL,
    CONSTRAINT pk_conversation_members PRIMARY KEY (conversation_id, member_id),
    CONSTRAINT fk_conmem_on_conversation FOREIGN KEY (conversation_id) REFERENCES conversations (id),
    CONSTRAINT fk_conmem_on_member FOREIGN KEY (member_id) REFERENCES members (id)
);

-- Messages table with proper inheritance using discriminator column
CREATE TABLE messages
(
    id              UUID NOT NULL,
    message_type    VARCHAR(31),
    conversation_id UUID NOT NULL,
    author_id       UUID NOT NULL,
    timestamp       TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_messages PRIMARY KEY (id),
    CONSTRAINT FK_MESSAGES_ON_AUTHOR FOREIGN KEY (author_id) REFERENCES members (id),
    CONSTRAINT FK_MESSAGES_ON_CONVERSATION FOREIGN KEY (conversation_id) REFERENCES conversations (id)
);

-- Separate tables for message subtypes (cleaner than single table)
CREATE TABLE text_messages
(
    id           UUID NOT NULL,
    text_content TEXT,
    CONSTRAINT pk_text_messages PRIMARY KEY (id),
    CONSTRAINT fk_text_messages_parent FOREIGN KEY (id) REFERENCES messages (id) ON DELETE CASCADE
);

CREATE TABLE photo_messages
(
    id         UUID NOT NULL,
    photo_name VARCHAR(255),
    CONSTRAINT pk_photo_messages PRIMARY KEY (id),
    CONSTRAINT fk_photo_messages_parent FOREIGN KEY (id) REFERENCES messages (id) ON DELETE CASCADE
);

CREATE TABLE file_messages
(
    id        UUID NOT NULL,
    file_name VARCHAR(255),
    CONSTRAINT pk_file_messages PRIMARY KEY (id),
    CONSTRAINT fk_file_messages_parent FOREIGN KEY (id) REFERENCES messages (id) ON DELETE CASCADE
);