package blog.jungmini.me.database.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Builder;
import lombok.Getter;

@Getter
@Table(name = "follows")
public class FollowEntity extends BaseEntity {
    @Id
    private Long followId;

    private Long followerId;
    private Long followeeId;

    @Builder
    public FollowEntity(Long followId, Long followerId, Long followeeId) {
        this.followId = followId;
        this.followerId = followerId;
        this.followeeId = followeeId;
    }

    public static FollowEntity of(Long followerId, Long followeeId) {
        return FollowEntity.builder()
                .followerId(followerId)
                .followeeId(followeeId)
                .build();
    }
}
