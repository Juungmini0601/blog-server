package blog.jungmini.me.database.projection;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostDetail {
    private Long postId;
    private Long userId;
    private String userNickname;
    private String userProfileImageUrl;
    private String userIntroduction;
    private String title;
    private String content;
    private String thumbnailUrl;
    private Boolean isPublic;
    private Long likeCount;
    private Long seriesId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isLiked;

    public void setIsLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }
}
