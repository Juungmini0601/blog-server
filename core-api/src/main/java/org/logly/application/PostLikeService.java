package org.logly.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.logly.database.entity.PostEntity;
import org.logly.database.entity.PostLikeEntity;
import org.logly.database.entity.UserEntity;
import org.logly.database.repository.PostLikeRepository;
import org.logly.database.repository.PostRepository;
import org.logly.database.repository.UserRepository;
import org.logly.error.CustomException;
import org.logly.error.ErrorType;

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

        if (postLikeRepository.existsByPostAndUser(post, user)) {
            throw new CustomException(ErrorType.VALIDATION_ERROR, "이미 좋아요한 게시물 입니다.");
        }

        PostLikeEntity postLike = PostLikeEntity.of(post, user);
        return postLikeRepository.save(postLike);
    }

    @Transactional
    public void disLike(Long userId, Long postId) {
        UserEntity user = userRepository.findByIdOrElseThrow(userId);
        PostEntity post = postRepository.findByIdOrElseThrow(postId);
        PostLikeEntity like = postLikeRepository.findByPostIdAndUserIdOrElseThrow(post, user);

        postLikeRepository.delete(like);
    }
}
