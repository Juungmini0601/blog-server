package org.logly.database.entity;

import jakarta.persistence.*;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "post_statistics")
public class PostStatisticsEntity extends BaseEntity {
    @Id
    @Column(name = "post_id")
    private Long postId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "post_id")
    private PostEntity post;

    private Long viewCount;
    private Long likeCount;
    private Long commentCount;

    public void incrementViewCount() {
        this.viewCount++;
    }

    public void incrementLikeCount() {
        this.likeCount++;
    }

    public void incrementCommentCount() {
        this.commentCount++;
    }

    public void decrementViewCount() {
        this.viewCount--;
    }

    public void decrementLikeCount() {
        this.likeCount--;
    }

    public void decrementCommentCount() {
        this.commentCount--;
    }

    @PrePersist
    void prePersist() {
        if (this.viewCount == null) {
            this.viewCount = 0L;
        }

        if (this.likeCount == null) {
            this.likeCount = 0L;
        }

        if (this.commentCount == null) {
            this.commentCount = 0L;
        }
    }
}
