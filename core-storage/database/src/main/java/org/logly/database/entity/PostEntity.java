package org.logly.database.entity;

import jakarta.persistence.*;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "posts")
public class PostEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String title;
    private String content;
    private String thumbnailUrl;
    private Boolean isPublic = true;
    private Long viewCount;
    private Long likeCount;
    private Long commentCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id", nullable = true)
    private SeriesEntity series;

    public boolean isAuthor(UserEntity userEntity) {
        return this.user.equals(userEntity);
    }

    @PrePersist
    void prePersist() {
        if (this.likeCount == null) {
            this.likeCount = 0L;
        }

        if (this.commentCount == null) {
            this.commentCount = 0L;
        }

        if (this.viewCount == null) {
            this.viewCount = 0L;
        }
    }
}
