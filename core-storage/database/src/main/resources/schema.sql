-- logly 스키마 생성
CREATE SCHEMA IF NOT EXISTS logly;
SET search_path TO logly, public;

CREATE TYPE logly.notification_type AS ENUM ('COMMENT', 'LIKE', 'FOLLOW');
CREATE TYPE logly.oauth_provider AS ENUM ('GOOGLE', 'GITHUB');

CREATE TABLE IF NOT EXISTS logly.users
(
    user_id           BIGSERIAL PRIMARY KEY,
    email             VARCHAR(255)       NOT NULL,
    nickname          VARCHAR(20)        NOT NULL,
    password          VARCHAR(255)       NOT NULL,
    profile_image_url VARCHAR(255),
    github_url        VARCHAR(255),
    introduction      VARCHAR(100),
    created_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT users_email_unique UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS logly.oauths
(
    oauth_id    BIGSERIAL PRIMARY KEY,
    user_id     BIGINT                            NOT NULL,
    provider    logly.oauth_provider              NOT NULL,
    provider_id VARCHAR(255)                      NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT oauths_provider_id_unique UNIQUE (provider, provider_id)
);

CREATE TABLE IF NOT EXISTS logly.follows
(
    follow_id   BIGSERIAL PRIMARY KEY,
    follower_id BIGINT                            NOT NULL, -- 팔로우 당하는 유저의 아이디
    followee_id BIGINT                            NOT NULL, -- 팔로우 하는 유저의 아이디
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT follows_follower_followee_unique UNIQUE (follower_id, followee_id)
);

CREATE TABLE IF NOT EXISTS logly.posts
(
    post_id       BIGSERIAL PRIMARY KEY,
    user_id       BIGINT                            NOT NULL,
    title         VARCHAR(255)                      NOT NULL,
    content       TEXT                              NOT NULL,
    thumbnail_url VARCHAR(255),
    is_public     BOOLEAN                           NOT NULL DEFAULT TRUE,
    view_count    BIGINT                            NOT NULL DEFAULT 0, -- 별도의 테이블로 분리 예정
    like_count    BIGINT                            NOT NULL DEFAULT 0, -- 별도의 테이블로 분리 예정
    series_id     BIGINT,
    created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS logly.post_drafts
(
    post_draft_id BIGSERIAL PRIMARY KEY,
    user_id       BIGINT                            NOT NULL,
    title         VARCHAR(255)                      NOT NULL,
    content       TEXT                              NOT NULL,
    created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS logly.tags
(
    tag_id     BIGSERIAL PRIMARY KEY,
    name       VARCHAR(50) NOT NULL UNIQUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS logly.post_tags
(
    post_tag_id BIGSERIAL PRIMARY KEY,
    post_id     BIGINT NOT NULL,
    tag_id      BIGINT NOT NULL,
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS logly.post_likes
(
    post_like_id BIGSERIAL PRIMARY KEY,
    post_id      BIGINT NOT NULL,
    user_id      BIGINT NOT NULL,
    created_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT post_likes_post_user_unique UNIQUE (post_id, user_id)
);

CREATE TABLE IF NOT EXISTS logly.post_views
(
    post_view_id BIGSERIAL PRIMARY KEY,
    post_id      BIGINT NOT NULL,
    user_id      BIGINT,
    created_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS logly.series
(
    series_id  BIGSERIAL PRIMARY KEY,
    user_id    BIGINT       NOT NULL,
    name       VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS logly.comments
(
    comment_id BIGSERIAL PRIMARY KEY,
    post_id    BIGINT NOT NULL,
    user_id    BIGINT NOT NULL,
    content    TEXT   NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS logly.comment_replies
(
    comment_reply_id BIGSERIAL PRIMARY KEY,
    comment_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS logly.notifications
(
    notification_id BIGSERIAL PRIMARY KEY,
    user_id         BIGINT                             NOT NULL,
    type            logly.notification_type            NOT NULL,
    content         VARCHAR(255)                       NOT NULL,
    is_read         BOOLEAN  DEFAULT FALSE,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 외래키 제약조건 추가
-- oauths 테이블
ALTER TABLE logly.oauths ADD CONSTRAINT fk_oauths_user_id
    FOREIGN KEY (user_id) REFERENCES logly.users(user_id) ON DELETE CASCADE;

-- follows 테이블
ALTER TABLE logly.follows ADD CONSTRAINT fk_follows_follower_id
    FOREIGN KEY (follower_id) REFERENCES logly.users(user_id) ON DELETE CASCADE;
ALTER TABLE logly.follows ADD CONSTRAINT fk_follows_followee_id
    FOREIGN KEY (followee_id) REFERENCES logly.users(user_id) ON DELETE CASCADE;

-- posts 테이블
ALTER TABLE logly.posts ADD CONSTRAINT fk_posts_user_id
    FOREIGN KEY (user_id) REFERENCES logly.users(user_id) ON DELETE CASCADE;
ALTER TABLE logly.posts ADD CONSTRAINT fk_posts_series_id
    FOREIGN KEY (series_id) REFERENCES logly.series(series_id) ON DELETE SET NULL;

-- post_drafts 테이블
ALTER TABLE logly.post_drafts ADD CONSTRAINT fk_post_drafts_user_id
    FOREIGN KEY (user_id) REFERENCES logly.users(user_id) ON DELETE CASCADE;

-- post_tags 테이블
ALTER TABLE logly.post_tags ADD CONSTRAINT fk_post_tags_post_id
    FOREIGN KEY (post_id) REFERENCES logly.posts(post_id) ON DELETE CASCADE;
ALTER TABLE logly.post_tags ADD CONSTRAINT fk_post_tags_tag_id
    FOREIGN KEY (tag_id) REFERENCES logly.tags(tag_id) ON DELETE CASCADE;

-- post_likes 테이블
ALTER TABLE logly.post_likes ADD CONSTRAINT fk_post_likes_post_id
    FOREIGN KEY (post_id) REFERENCES logly.posts(post_id) ON DELETE CASCADE;
ALTER TABLE logly.post_likes ADD CONSTRAINT fk_post_likes_user_id
    FOREIGN KEY (user_id) REFERENCES logly.users(user_id) ON DELETE CASCADE;

-- post_views 테이블
ALTER TABLE logly.post_views ADD CONSTRAINT fk_post_views_post_id
    FOREIGN KEY (post_id) REFERENCES logly.posts(post_id) ON DELETE CASCADE;
ALTER TABLE logly.post_views ADD CONSTRAINT fk_post_views_user_id
    FOREIGN KEY (user_id) REFERENCES logly.users(user_id) ON DELETE SET NULL;

-- series 테이블
ALTER TABLE logly.series ADD CONSTRAINT fk_series_user_id
    FOREIGN KEY (user_id) REFERENCES logly.users(user_id) ON DELETE CASCADE;

-- comments 테이블
ALTER TABLE logly.comments ADD CONSTRAINT fk_comments_post_id
    FOREIGN KEY (post_id) REFERENCES logly.posts(post_id) ON DELETE CASCADE;
ALTER TABLE logly.comments ADD CONSTRAINT fk_comments_user_id
    FOREIGN KEY (user_id) REFERENCES logly.users(user_id) ON DELETE CASCADE;

-- comment_replies 테이블
ALTER TABLE logly.comment_replies ADD CONSTRAINT fk_comment_replies_comment_id
    FOREIGN KEY (comment_id) REFERENCES logly.comments(comment_id) ON DELETE CASCADE;
ALTER TABLE logly.comment_replies ADD CONSTRAINT fk_comment_replies_user_id
    FOREIGN KEY (user_id) REFERENCES logly.users(user_id) ON DELETE CASCADE;

-- notifications 테이블
ALTER TABLE logly.notifications ADD CONSTRAINT fk_notifications_user_id
    FOREIGN KEY (user_id) REFERENCES logly.users(user_id) ON DELETE CASCADE;
