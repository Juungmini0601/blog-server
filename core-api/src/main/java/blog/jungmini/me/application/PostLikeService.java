package blog.jungmini.me.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import blog.jungmini.me.common.error.CustomException;
import blog.jungmini.me.common.error.ErrorType;
import blog.jungmini.me.database.entity.PostEntity;
import blog.jungmini.me.database.entity.PostLikeEntity;
import blog.jungmini.me.database.entity.UserEntity;
import blog.jungmini.me.database.repository.PostLikeRepository;
import blog.jungmini.me.database.repository.PostRepository;
import blog.jungmini.me.database.repository.UserRepository;

@Service
public class PostLikeService {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;

    public PostLikeService(
            PostRepository postRepository, PostLikeRepository postLikeRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.postLikeRepository = postLikeRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public PostLikeEntity like(Long userId, Long postId) {
        UserEntity user = userRepository.findByIdOrElseThrow(userId);
        PostEntity post = postRepository.findByIdOrElseThrow(postId);

        if (postLikeRepository.existsByPostIdAndUserId(postId, userId)) {
            throw new CustomException(ErrorType.VALIDATION_ERROR, "이미 좋아요한 게시물 입니다.");
        }

        PostLikeEntity postLike = PostLikeEntity.of(post, user);
        return postLikeRepository.save(postLike);
    }

    @Transactional
    public void disLike(Long userId, Long postId) {
        PostLikeEntity like = postLikeRepository.findByPostIdAndUserIdOrElseThrow(postId, userId);
        postLikeRepository.delete(like);
    }
}
