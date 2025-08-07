package blog.jungmini.me.database.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Getter;

@Getter
@Table(name = "post_views")
public class PostViewEntity extends BaseEntity {
    @Id
    private Long postViewId;

    private Long postId;

    private Long userId;

    // For JPA
    protected PostViewEntity() {}
}
