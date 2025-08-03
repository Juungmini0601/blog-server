CREATE TABLE IF NOT EXISTS `users`
(
    `user_id`           BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    `email`             VARCHAR(255)       NOT NULL,
    `nickname`          VARCHAR(20)        NOT NULL,
    `password`          VARCHAR(255)       NOT NULL,
    `profile_image_url` VARCHAR(255),
    `github_url`        VARCHAR(255),
    `introduction`      VARCHAR(100),
    `created_at`        DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at`        DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `oauths`
(
    `oauth_id`    BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    `user_id`     BIGINT                            NOT NULL,
    `provider`    VARCHAR(20)                       NOT NULL,
    `provider_id` VARCHAR(255)                      NOT NULL,
    `created_at`  DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY `provider_id` (`provider`, `provider_id`)
);

CREATE TABLE IF NOT EXISTS `follows`
(
    `follow_id`   BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    `follower_id` BIGINT                            NOT NULL, -- 팔로우 당하는 유저의 아이디
    `followee_id` BIGINT                            NOT NULL, -- 팔로우 하는 유저의 아이디
    `created_at`  DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY `follower_id_followee_id` (`follower_id`, `followee_id`)
) DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `posts`
(
    `post_id`       BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    `user_id`       BIGINT                            NOT NULL,
    `title`         VARCHAR(255)                      NOT NULL,
    `content`       TEXT                              NOT NULL,
    `thumbnail_url` VARCHAR(255),
    `is_public`     BOOLEAN                           NOT NULL DEFAULT TRUE,
    `view_count`    BIGINT                            NOT NULL DEFAULT 0,
    `series_id`     BIGINT,
    `created_at`    DATETIME                                   DEFAULT CURRENT_TIMESTAMP,
    `updated_at`    DATETIME                                   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `post_drafts`
(
    `post_draft_id` BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    `user_id`       BIGINT                            NOT NULL,
    `title`         VARCHAR(255)                      NOT NULL,
    `content`       TEXT                              NOT NULL,
    `created_at`    DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at`    DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE tags
(
    `tag_id`     BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name`       VARCHAR(50) NOT NULL UNIQUE,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) DEFAULT CHARSET = utf8mb4;

CREATE TABLE post_tags
(
    `post_tag_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `post_id`     BIGINT NOT NULL,
    `tag_id`      BIGINT NOT NULL,
    `created_at`  DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) DEFAULT CHARSET = utf8mb4;

CREATE TABLE post_likes
(
    `post_like_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `post_id`      BIGINT NOT NULL,
    `user_id`      BIGINT NOT NULL,
    `created_at`   DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at`   DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uq_post_user (post_id, user_id)
) DEFAULT CHARSET = utf8mb4;

CREATE TABLE post_views
(
    `post_view_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `post_id`      BIGINT NOT NULL,
    `user_id`      BIGINT,
    `created_at`   DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at`   DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) DEFAULT CHARSET = utf8mb4;

CREATE TABLE series
(
    series_id  BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT       NOT NULL,
    name       VARCHAR(100) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) DEFAULT CHARSET = utf8mb4;

CREATE TABLE comments
(
    `comments_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `post_id`     BIGINT NOT NULL,
    `user_id`     BIGINT NOT NULL,
    `parent_id`   BIGINT,
    `content`     TEXT   NOT NULL,
    `created_at`  DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at`  DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) DEFAULT CHARSET = utf8mb4;

CREATE TABLE notifications
(
    `notification_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id`         BIGINT                             NOT NULL,
    `type`            ENUM ('COMMENT', 'LIKE', 'FOLLOW') NOT NULL,
    `content`         VARCHAR(255)                       NOT NULL,
    `is_read`         BOOLEAN  DEFAULT FALSE,
    `created_at`      DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at`      DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) DEFAULT CHARSET = utf8mb4;