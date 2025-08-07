package blog.jungmini.me.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import blog.jungmini.me.common.error.CustomException;
import blog.jungmini.me.common.error.ErrorType;
import blog.jungmini.me.common.response.CursorResponse;
import blog.jungmini.me.database.entity.FollowEntity;
import blog.jungmini.me.database.projection.UserFollowItem;
import blog.jungmini.me.database.repository.FollowRepository;
import blog.jungmini.me.database.repository.UserRepository;

@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    private static final int PAGE_SIZE = 20;

    public FollowService(FollowRepository followRepository, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    /**
     * followerId를 팔로우 하고 있는 유저 아이디 반환
     * @param userId 팔로우 당하는 유저의 아이디
     */
    public CursorResponse<UserFollowItem, Long> getFollowerList(Long userId, Long lastFollowId) {
        List<Long> followerIds = followRepository.findFollowersByFollowerIdWithPaging(userId, lastFollowId);

        if (followerIds.isEmpty()) {
            return CursorResponse.of(List.of(), null, false);
        }

        List<UserFollowItem> followItems = userRepository.findUserFollowItemsByIds(followerIds);
        Long nextCursor = followerIds.get(followerIds.size() - 1);
        boolean hasNext = followerIds.size() == PAGE_SIZE;
        return CursorResponse.of(followItems, nextCursor, hasNext);
    }

    public CursorResponse<UserFollowItem, Long> getFolloweeList(Long userId, Long lastFollowId) {
        List<Long> followeeIds = followRepository.findFolloweeIdsByFollowerId(userId, lastFollowId);

        if (followeeIds.isEmpty()) {
            return CursorResponse.of(List.of(), null, false);
        }

        List<UserFollowItem> followItems = userRepository.findUserFollowItemsByIds(followeeIds);
        Long nextCursor = followeeIds.get(followeeIds.size() - 1);
        boolean hasNext = followeeIds.size() == PAGE_SIZE;
        return CursorResponse.of(followItems, nextCursor, hasNext);
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
