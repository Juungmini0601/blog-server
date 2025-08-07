package blog.jungmini.me.database.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Getter;

@Getter
@Table(name = "post_likes")
public class PostLikeEntity extends BaseEntity {
    @Id
    private Long postLikeId;

    private Long postId;
    private Long userId;

    public static PostLikeEntity of(PostEntity postEntity, UserEntity userEntity) {
        PostLikeEntity entity = new PostLikeEntity();
        entity.postId = postEntity.getPostId();
        entity.userId = userEntity.getUserId();

        return entity;
    }
}
