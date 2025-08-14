package blog.jungmini.me.database.projection;

import java.time.LocalDateTime;

public record CommentItem(
        Long commentId,
        Long postId,
        String content,
        LocalDateTime createdAt,
        Long userId,
        String nickname,
        String profileImageUrl) {}
