package org.logly.database.projection;

import java.time.LocalDateTime;

public record PostItem(
        Long postId,
        String thumbnailUrl,
        LocalDateTime createdAt,
        String content,
        Long userId,
        String nickname,
        String profileImageUrl,
        Long commentCount,
        Long likeCount) {}
