package blog.jungmini.me.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import blog.jungmini.me.database.entity.FollowEntity;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, Long> {
    boolean existsByFollowerIdAndFolloweeId(Long followerId, Long followeeId);
}
