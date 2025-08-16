package org.logly.database.entity;

import jakarta.persistence.*;

import lombok.Getter;

@Getter
@Table(name = "post_likes")
@Entity
public class PostLikeEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postLikeId;

    private Long postId;
    private Long userId;

    protected PostLikeEntity() {}

    public static PostLikeEntity of(PostEntity postEntity, UserEntity userEntity) {
        PostLikeEntity entity = new PostLikeEntity();
        entity.postId = postEntity.getPostId();
        entity.userId = userEntity.getUserId();

        return entity;
    }
}
