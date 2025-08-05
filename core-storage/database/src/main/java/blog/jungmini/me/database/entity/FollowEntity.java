package blog.jungmini.me.database.entity;

import jakarta.persistence.*;

import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
@Table(name = "follows")
public class FollowEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long followId;

    @Column(name = "follower_id", nullable = false)
    private Long followerId;

    @Column(name = "followee_id", nullable = false)
    private Long followeeId;

    // For JPA
    protected FollowEntity() {}

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
