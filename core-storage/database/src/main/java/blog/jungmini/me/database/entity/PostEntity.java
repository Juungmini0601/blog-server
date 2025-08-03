package blog.jungmini.me.database.entity;

import jakarta.persistence.*;

import lombok.Getter;

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
}
