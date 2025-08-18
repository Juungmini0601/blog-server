-- logly 스키마 생성
CREATE SCHEMA IF NOT EXISTS logly;
SET search_path TO logly, public;

CREATE TYPE logly.notification_type AS ENUM ('COMMENT', 'LIKE', 'FOLLOW');
CREATE TYPE logly.oauth_provider AS ENUM ('GOOGLE', 'GITHUB');

CREATE TABLE IF NOT EXISTS logly.users
(
    user_id           BIGSERIAL PRIMARY KEY,
    email             VARCHAR(255) NOT NULL,
    nickname          VARCHAR(20)  NOT NULL,
    password          VARCHAR(255) NOT NULL,
    profile_image_url VARCHAR(255),
    github_url        VARCHAR(255),
    introduction      VARCHAR(100),
    follower_count    BIGINT       NOT NULL DEFAULT 0,
    followee_count    BIGINT       NOT NULL DEFAULT 0,
    created_at        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT users_email_unique UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS logly.oauths
(
    oauth_id    BIGSERIAL PRIMARY KEY,
    user_id     BIGINT               NOT NULL,
    provider    logly.oauth_provider NOT NULL,
    provider_id VARCHAR(255)         NOT NULL,
    created_at  TIMESTAMP            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT oauths_provider_id_unique UNIQUE (provider, provider_id),
    CONSTRAINT fk_oauth_user_id FOREIGN KEY (user_id) REFERENCES logly.users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS logly.follows
(
    follow_id   BIGSERIAL PRIMARY KEY,
    follower_id BIGINT    NOT NULL, -- 팔로우 당하는 유저의 아이디
    followee_id BIGINT    NOT NULL, -- 팔로우 하는 유저의 아이디
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT follows_follower_followee_unique UNIQUE (follower_id, followee_id),
    CONSTRAINT fk_follow_follower_id FOREIGN KEY (follower_id) REFERENCES logly.users (user_id) ON DELETE CASCADE,
    CONSTRAINT fk_follow_followee_id FOREIGN KEY (followee_id) REFERENCES logly.users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS logly.series
(
    series_id  BIGSERIAL PRIMARY KEY,
    user_id    BIGINT       NOT NULL,
    name       VARCHAR(100) NOT NULL,
    post_count BIGINT       NOT NULL DEFAULT 0,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_series_user_id FOREIGN KEY (user_id) REFERENCES logly.users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS logly.posts
(
    post_id       BIGSERIAL PRIMARY KEY,
    user_id       BIGINT       NOT NULL,
    title         VARCHAR(255) NOT NULL,
    content       TEXT         NOT NULL,
    thumbnail_url VARCHAR(255),
    is_public     BOOLEAN      NOT NULL DEFAULT TRUE,
    series_id     BIGINT,
    view_count    BIGINT       NOT NULL DEFAULT 0,
    like_count    BIGINT       NOT NULL DEFAULT 0,
    comment_count BIGINT       NOT NULL DEFAULT 0,
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_post_user_id FOREIGN KEY (user_id) REFERENCES logly.users (user_id) ON DELETE CASCADE,
    CONSTRAINT fk_post_series_id FOREIGN KEY (series_id) REFERENCES logly.series (series_id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS logly.post_drafts
(
    post_draft_id BIGSERIAL PRIMARY KEY,
    user_id       BIGINT       NOT NULL,
    title         VARCHAR(255) NOT NULL,
    content       TEXT         NOT NULL,
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_post_draft_user_id FOREIGN KEY (user_id) REFERENCES logly.users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS logly.tags
(
    tag_id     BIGSERIAL PRIMARY KEY,
    name       VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS logly.post_tags
(
    post_tag_id BIGSERIAL PRIMARY KEY,
    post_id     BIGINT    NOT NULL,
    tag_id      BIGINT    NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT post_tags_post_tag_unique UNIQUE (post_id, tag_id),
    CONSTRAINT fk_post_tag_post_id FOREIGN KEY (post_id) REFERENCES logly.posts (post_id) ON DELETE CASCADE,
    CONSTRAINT fk_post_tag_tag_id FOREIGN KEY (tag_id) REFERENCES logly.tags (tag_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS logly.post_likes
(
    post_like_id BIGSERIAL PRIMARY KEY,
    post_id      BIGINT    NOT NULL,
    user_id      BIGINT    NOT NULL,
    created_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT post_likes_post_user_unique UNIQUE (post_id, user_id),
    CONSTRAINT fk_post_like_post_id FOREIGN KEY (post_id) REFERENCES logly.posts (post_id) ON DELETE CASCADE,
    CONSTRAINT fk_post_like_user_id FOREIGN KEY (user_id) REFERENCES logly.users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS logly.post_views
(
    post_view_id BIGSERIAL PRIMARY KEY,
    post_id      BIGINT    NOT NULL,
    user_id      BIGINT,
    created_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT post_views_post_user_unique UNIQUE (post_id, user_id),
    CONSTRAINT fk_post_view_post_id FOREIGN KEY (post_id) REFERENCES logly.posts (post_id) ON DELETE CASCADE,
    CONSTRAINT fk_post_view_user_id FOREIGN KEY (user_id) REFERENCES logly.users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS logly.comments
(
    comment_id BIGSERIAL PRIMARY KEY,
    post_id    BIGINT    NOT NULL,
    user_id    BIGINT    NOT NULL,
    content    TEXT      NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT comments_post_user_unique UNIQUE (post_id, user_id),
    CONSTRAINT fk_comment_post_id FOREIGN KEY (post_id) REFERENCES logly.posts (post_id) ON DELETE CASCADE,
    CONSTRAINT fk_comment_user_id FOREIGN KEY (user_id) REFERENCES logly.users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS logly.comment_replies
(
    comment_reply_id BIGSERIAL PRIMARY KEY,
    comment_id       BIGINT    NOT NULL,
    user_id          BIGINT    NOT NULL,
    content          TEXT      NOT NULL,
    created_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT comment_replies_comment_user_unique UNIQUE (comment_id, user_id),
    CONSTRAINT fk_comment_reply_comment_id FOREIGN KEY (comment_id) REFERENCES logly.comments (comment_id) ON DELETE CASCADE,
    CONSTRAINT fk_comment_reply_user_id FOREIGN KEY (user_id) REFERENCES logly.users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS logly.notifications
(
    notification_id BIGSERIAL PRIMARY KEY,
    user_id         BIGINT                  NOT NULL,
    type            logly.notification_type NOT NULL,
    content         VARCHAR(255)            NOT NULL,
    is_read         BOOLEAN                          DEFAULT FALSE,
    created_at      TIMESTAMP               NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP               NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Spring Batch
CREATE TABLE BATCH_JOB_INSTANCE
(
    JOB_INSTANCE_ID BIGINT       NOT NULL PRIMARY KEY,
    VERSION         BIGINT,
    JOB_NAME        VARCHAR(100) NOT NULL,
    JOB_KEY         VARCHAR(32)  NOT NULL,
    constraint JOB_INST_UN unique (JOB_NAME, JOB_KEY)
);

CREATE TABLE BATCH_JOB_EXECUTION
(
    JOB_EXECUTION_ID BIGINT    NOT NULL PRIMARY KEY,
    VERSION          BIGINT,
    JOB_INSTANCE_ID  BIGINT    NOT NULL,
    CREATE_TIME      TIMESTAMP NOT NULL,
    START_TIME       TIMESTAMP DEFAULT NULL,
    END_TIME         TIMESTAMP DEFAULT NULL,
    STATUS           VARCHAR(10),
    EXIT_CODE        VARCHAR(2500),
    EXIT_MESSAGE     VARCHAR(2500),
    LAST_UPDATED     TIMESTAMP,
    constraint JOB_INST_EXEC_FK foreign key (JOB_INSTANCE_ID)
        references BATCH_JOB_INSTANCE (JOB_INSTANCE_ID)
);

CREATE TABLE BATCH_JOB_EXECUTION_PARAMS
(
    JOB_EXECUTION_ID BIGINT       NOT NULL,
    PARAMETER_NAME   VARCHAR(100) NOT NULL,
    PARAMETER_TYPE   VARCHAR(100) NOT NULL,
    PARAMETER_VALUE  VARCHAR(2500),
    IDENTIFYING      CHAR(1)      NOT NULL,
    constraint JOB_EXEC_PARAMS_FK foreign key (JOB_EXECUTION_ID)
        references BATCH_JOB_EXECUTION (JOB_EXECUTION_ID)
);

CREATE TABLE BATCH_STEP_EXECUTION
(
    STEP_EXECUTION_ID  BIGINT       NOT NULL PRIMARY KEY,
    VERSION            BIGINT       NOT NULL,
    STEP_NAME          VARCHAR(100) NOT NULL,
    JOB_EXECUTION_ID   BIGINT       NOT NULL,
    CREATE_TIME        TIMESTAMP    NOT NULL,
    START_TIME         TIMESTAMP DEFAULT NULL,
    END_TIME           TIMESTAMP DEFAULT NULL,
    STATUS             VARCHAR(10),
    COMMIT_COUNT       BIGINT,
    READ_COUNT         BIGINT,
    FILTER_COUNT       BIGINT,
    WRITE_COUNT        BIGINT,
    READ_SKIP_COUNT    BIGINT,
    WRITE_SKIP_COUNT   BIGINT,
    PROCESS_SKIP_COUNT BIGINT,
    ROLLBACK_COUNT     BIGINT,
    EXIT_CODE          VARCHAR(2500),
    EXIT_MESSAGE       VARCHAR(2500),
    LAST_UPDATED       TIMESTAMP,
    constraint JOB_EXEC_STEP_FK foreign key (JOB_EXECUTION_ID)
        references BATCH_JOB_EXECUTION (JOB_EXECUTION_ID)
);

CREATE TABLE BATCH_STEP_EXECUTION_CONTEXT
(
    STEP_EXECUTION_ID  BIGINT        NOT NULL PRIMARY KEY,
    SHORT_CONTEXT      VARCHAR(2500) NOT NULL,
    SERIALIZED_CONTEXT TEXT,
    constraint STEP_EXEC_CTX_FK foreign key (STEP_EXECUTION_ID)
        references BATCH_STEP_EXECUTION (STEP_EXECUTION_ID)
);

CREATE TABLE BATCH_JOB_EXECUTION_CONTEXT
(
    JOB_EXECUTION_ID   BIGINT        NOT NULL PRIMARY KEY,
    SHORT_CONTEXT      VARCHAR(2500) NOT NULL,
    SERIALIZED_CONTEXT TEXT,
    constraint JOB_EXEC_CTX_FK foreign key (JOB_EXECUTION_ID)
        references BATCH_JOB_EXECUTION (JOB_EXECUTION_ID)
);

CREATE SEQUENCE BATCH_STEP_EXECUTION_SEQ MAXVALUE 9223372036854775807 NO CYCLE;
CREATE SEQUENCE BATCH_JOB_EXECUTION_SEQ MAXVALUE 9223372036854775807 NO CYCLE;
CREATE SEQUENCE BATCH_JOB_SEQ MAXVALUE 9223372036854775807 NO CYCLE;