package org.logly.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.logly.database.entity.PostEntity;
import org.logly.database.entity.SeriesEntity;
import org.logly.database.entity.UserEntity;
import org.logly.database.projection.PostDetail;
import org.logly.database.projection.PostItem;
import org.logly.database.repository.PostLikeRepository;
import org.logly.database.repository.PostRepository;
import org.logly.database.repository.SeriesRepository;
import org.logly.database.repository.UserRepository;
import org.logly.error.CustomException;
import org.logly.error.ErrorType;
import org.logly.response.CursorResponse;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final SeriesRepository seriesRepository;

    private static final int PAGE_SIZE = 20;

    public PostService(
            PostRepository postRepository,
            PostLikeRepository postLikeRepository,
            UserRepository userRepository,
            SeriesRepository seriesRepository) {
        this.postRepository = postRepository;
        this.postLikeRepository = postLikeRepository;
        this.userRepository = userRepository;
        this.seriesRepository = seriesRepository;
    }

    @Transactional(readOnly = true)
    public CursorResponse<PostItem, Long> getPostList(Long lastPostId) {
        List<PostItem> posts = postRepository.findPosts(lastPostId);
        if (posts.isEmpty()) {
            return CursorResponse.of(posts, null, false);
        }

        Long nextCursor = posts.get(posts.size() - 1).postId();
        boolean hasNext = posts.size() == PAGE_SIZE;
        return CursorResponse.of(posts, nextCursor, hasNext);
    }

    @Transactional(readOnly = true)
    public PostDetail getById(Long postId, Long userId) {
        PostDetail postDetail = postRepository.findPostDetailByIdOrElseThrow(postId);

        if (userId != null) {
            boolean isLiked = postLikeRepository.existsByPostIdAndUserId(postId, userId);
            postDetail.setIsLiked(isLiked);
        }

        return postDetail;
    }

    @Transactional
    public PostEntity create(Long userId, PostEntity postEntity) {
        UserEntity author = userRepository.findByIdOrElseThrow(userId);
        postEntity.setAuthor(author);

        if (postEntity.getSeriesId() != null) {
            SeriesEntity series = seriesRepository.findByIdOrElseThrow(postEntity.getSeriesId());
            postEntity.setSeries(series);
        }

        return postRepository.save(postEntity);
    }

    @Transactional
    public PostEntity update(Long userId, PostEntity updatePost) {
        UserEntity author = userRepository.findByIdOrElseThrow(userId);
        PostEntity post = postRepository.findByIdOrElseThrow(updatePost.getPostId());

        if (!post.isAuthor(author)) {
            throw new CustomException(ErrorType.AUTHORIZATION_ERROR, "작성자만 게시글을 수정 할 수 있습니다.");
        }

        post.setTitle(updatePost.getTitle());
        post.setContent(updatePost.getContent());
        post.setThumbnailUrl(updatePost.getThumbnailUrl());
        post.setPublic(updatePost.getIsPublic());

        if (post.getSeriesId() != null) {
            SeriesEntity series = seriesRepository.findByIdOrElseThrow(post.getSeriesId());
            post.setSeries(series);
        }

        return postRepository.save(post);
    }

    @Transactional
    public void remove(Long userId, Long postId) {
        UserEntity author = userRepository.findByIdOrElseThrow(userId);
        PostEntity post = postRepository.findByIdOrElseThrow(postId);

        if (!post.isAuthor(author)) {
            throw new CustomException(ErrorType.AUTHORIZATION_ERROR, "작성자만 게시글을 삭제 할 수 있습니다.");
        }

        postRepository.deleteById(postId);
    }
}
