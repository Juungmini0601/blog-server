package blog.jungmini.me.database.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import blog.jungmini.me.database.entity.FollowEntity;

@Repository
public interface FollowRepository extends CrudRepository<FollowEntity, Long> {
    boolean existsByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

    void deleteByFollowerIdAndFolloweeId(Long followerId, Long followeeId);
}
