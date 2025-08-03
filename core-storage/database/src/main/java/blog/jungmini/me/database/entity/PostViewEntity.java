package blog.jungmini.me.database.entity;

import jakarta.persistence.*;

import lombok.Getter;

@Getter
@Entity
@Table(name = "post_views")
public class PostViewEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_view_id")
    private Long postViewId;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(name = "user_id")
    private Long userId;

    // For JPA
    protected PostViewEntity() {}
}
