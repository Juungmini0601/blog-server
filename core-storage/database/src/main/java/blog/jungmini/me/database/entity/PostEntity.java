package blog.jungmini.me.database.entity;

import jakarta.persistence.*;

import lombok.Builder;
import lombok.Getter;

import blog.jungmini.me.common.error.CustomException;
import blog.jungmini.me.common.error.ErrorType;

@Getter
@Entity
@Table(name = "posts")
public class PostEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic = true;

    @Column(name = "view_count", nullable = false)
    private Long viewCount = 0L;

    @Column(name = "series_id")
    private Long seriesId;

    // For JPA
    protected PostEntity() {}

    @Builder
    public PostEntity(
            Long postId,
            Long userId,
            String title,
            String content,
            String thumbnailUrl,
            Boolean isPublic,
            Long viewCount,
            Long seriesId) {
        this.postId = postId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.thumbnailUrl = thumbnailUrl;
        this.isPublic = isPublic;
        this.viewCount = viewCount;
        this.seriesId = seriesId;
    }

    public void setAuthor(UserEntity userEntity) {
        this.userId = userEntity.getUserId();
    }

    public boolean isAuthor(UserEntity userEntity) {
        if (userId == null) {
            throw new CustomException(ErrorType.INTERNAL_SERVER_ERROR, "작성자 아이디가 Null 입니다.");
        }

        return userId.equals(userEntity.getUserId());
    }

    public void setSeries(SeriesEntity seriesEntity) {
        if (!seriesEntity.getUserId().equals(this.userId)) {
            throw new CustomException(ErrorType.VALIDATION_ERROR, "게시글은 자신의 시리즈에만 포함 할 수 있습니다.");
        }

        this.seriesId = seriesEntity.getSeriesId();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    public void setViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }
}
