package blog.jungmini.me.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import blog.jungmini.me.common.error.CustomException;
import blog.jungmini.me.common.error.ErrorType;
import blog.jungmini.me.database.entity.FollowEntity;
import blog.jungmini.me.database.repository.FollowRepository;

@Service
public class FollowService {

    private final FollowRepository followRepository;

    public FollowService(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }

    /**
     * @param followerId 팔로우 당하는 유저의 아이디
     * @param followeeId 팔로우 하는 유저의 아이디
     */
    @Transactional
    public FollowEntity follow(Long followerId, Long followeeId) {
        if (followerId.equals(followeeId)) {
            throw new CustomException(ErrorType.VALIDATION_ERROR, "자기 자신을 팔로우 할 수 없습니다.");
        }

        if (followRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId)) {
            throw new CustomException(ErrorType.VALIDATION_ERROR, "이미 팔로우 한 유저입니다.");
        }

        return followRepository.save(FollowEntity.of(followerId, followeeId));
    }

    /**
     * @param followerId 팔로우 당하는 유저의 아이디
     * @param followeeId 팔로우 하는 유저의 아이디
     */
    @Transactional
    public void unFollow(Long followerId, Long followeeId) {
        if (followerId.equals(followeeId)) {
            throw new CustomException(ErrorType.VALIDATION_ERROR, "자기 자신을 팔로우 취소 할 수 없습니다.");
        }

        if (!followRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId)) {
            throw new CustomException(ErrorType.VALIDATION_ERROR, "팔로우 정보가 존재하지 않습니다.");
        }

        followRepository.deleteByFollowerIdAndFolloweeId(followerId, followeeId);
    }
}
