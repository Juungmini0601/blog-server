package blog.jungmini.me.database.entity;

import jakarta.persistence.*;

import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@Table(name = "comments")
public class CommentEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    // For JPA
    protected CommentEntity() {}

    @Builder
    public CommentEntity(Long commentId, Long postId, Long userId, Long parentId, String content) {
        this.commentId = commentId;
        this.postId = postId;
        this.userId = userId;
        this.parentId = parentId;
        this.content = content;
    }

    public void setUser(UserEntity userEntity) {
        this.userId = userEntity.getUserId();
    }
}
