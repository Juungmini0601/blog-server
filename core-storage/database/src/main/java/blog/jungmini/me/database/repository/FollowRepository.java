package blog.jungmini.me.database.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import blog.jungmini.me.database.entity.FollowEntity;

@Repository
public interface FollowRepository extends CrudRepository<FollowEntity, Long> {

    @Query(
            """
                SELECT follower_id
                FROM follows
                WHERE followee_id = :followeeId
                AND follow_id < :lastFollowId
                ORDER BY follow_id DESC
                LIMIT 20
            """)
    List<Long> findFollowersByFollowerIdWithPaging(Long followeeId, Long lastFollowId);

    @Query(
            """
                SELECT followee_id
                FROM follows
                WHERE follower_id = :followerId
                AND follow_id < :lastFollowId
                ORDER BY follow_id DESC
                LIMIT 20
            """)
    List<Long> findFolloweeIdsByFollowerId(Long followerId, Long lastFollowId);

    boolean existsByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

    void deleteByFollowerIdAndFolloweeId(Long followerId, Long followeeId);
}
