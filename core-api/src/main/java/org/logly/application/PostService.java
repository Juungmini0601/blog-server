package org.logly.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import org.logly.database.entity.PostEntity;
import org.logly.database.entity.PostStatisticsEntity;
import org.logly.database.entity.SeriesEntity;
import org.logly.database.entity.UserEntity;
import org.logly.database.projection.PostDetail;
import org.logly.database.projection.PostItem;
import org.logly.database.repository.*;
import org.logly.dto.request.CreatePostRequest;
import org.logly.dto.request.UpdatePostRequest;
import org.logly.error.CustomException;
import org.logly.error.ErrorType;
import org.logly.response.CursorResponse;
import org.logly.security.model.CustomUserDetails;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final SeriesRepository seriesRepository;

    private static final int PAGE_SIZE = 20;

    @Transactional(readOnly = true)
    public CursorResponse<PostItem, Long> getPostList(Long lastPostId) {
        List<PostItem> posts = postRepository.findPosts(lastPostId);
        if (posts.isEmpty()) {
            return CursorResponse.of(posts, null, false);
        }

        Long nextCursor = posts.get(posts.size() - 1).getPostId();
        boolean hasNext = posts.size() == PAGE_SIZE;
        return CursorResponse.of(posts, nextCursor, hasNext);
    }

    @Transactional(readOnly = true)
    public CursorResponse<PostItem, Long> getPostListByUserId(Long userId, Long lastPostId) {
        UserEntity user = userRepository.findByIdOrElseThrow(userId);
        List<PostItem> postItems = postRepository.findPostItemsByUser(user, lastPostId);

        if (postItems.isEmpty()) {
            return CursorResponse.of(postItems, null, false);
        }

        Long nextCursor = postItems.get(postItems.size() - 1).getPostId();
        boolean hasNext = postItems.size() == PAGE_SIZE;
        return CursorResponse.of(postItems, nextCursor, hasNext);
    }

    @Transactional(readOnly = true)
    public CursorResponse<PostItem, Long> getPostListBySeriesId(Long seriesId, Long lastPostId) {
        SeriesEntity series = seriesRepository.findByIdOrElseThrow(seriesId);
        List<PostItem> postItems = postRepository.findPostItemsBySeries(series, lastPostId);

        if (postItems.isEmpty()) {
            return CursorResponse.of(postItems, null, false);
        }

        Long nextCursor = postItems.get(postItems.size() - 1).getPostId();
        boolean hasNext = postItems.size() == PAGE_SIZE;
        return CursorResponse.of(postItems, nextCursor, hasNext);
    }

    @Transactional(readOnly = true)
    public PostDetail getById(Long postId, CustomUserDetails details) {
        PostEntity post = postRepository.findByIdOrElseThrow(postId);
        PostDetail postDetail = PostDetail.from(post);

        if (details != null) {
            UserEntity requester = userRepository.findByIdOrElseThrow(details.getUserId());
            boolean isLiked = postLikeRepository.existsByPostAndUser(post, requester);
            postDetail.setIsLiked(isLiked);
        }

        return postDetail;
    }

    @Transactional
    public PostEntity create(CustomUserDetails details, CreatePostRequest request) {
        UserEntity author = userRepository.findByIdOrElseThrow(details.getUserId());
        PostEntity post = PostEntity.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .thumbnailUrl(request.getThumbnailUrl())
                .isPublic(request.isPublic())
                .user(author)
                .build();

        if (request.getSeriesId() != null) {
            SeriesEntity series = seriesRepository.findByIdOrElseThrow(request.getSeriesId());

            if (!series.getUser().equals(author)) {
                throw new CustomException(ErrorType.VALIDATION_ERROR, "다른 사람의 시리즈 입니다.");
            }

            post.setSeries(series);
            series.setPostCount(series.getPostCount() + 1);
            seriesRepository.save(series);
        }

        PostEntity savedPost = postRepository.save(post);

        PostStatisticsEntity statistics = PostStatisticsEntity.builder()
                .post(savedPost)
                .build();

        savedPost.setStatistics(statistics);

        return savedPost;
    }

    @Transactional
    public PostEntity update(CustomUserDetails details, Long postId, UpdatePostRequest request) {
        UserEntity author = userRepository.findByIdOrElseThrow(details.getUserId());
        PostEntity post = postRepository.findByIdOrElseThrow(postId);

        if (!post.isAuthor(author)) {
            throw new CustomException(ErrorType.AUTHORIZATION_ERROR, "작성자만 게시글을 수정 할 수 있습니다.");
        }

        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setThumbnailUrl(request.getThumbnailUrl());
        post.setIsPublic(request.isPublic());

        if (request.getSeriesId() != null) {
            SeriesEntity series = seriesRepository.findByIdOrElseThrow(request.getSeriesId());
            if (post.getSeries() != null && !post.getSeries().equals(series)) {
                post.getSeries().setPostCount(post.getSeries().getPostCount() - 1);
                seriesRepository.save(post.getSeries());
            }

            post.setSeries(series);
            series.setPostCount(series.getPostCount() + 1);
            seriesRepository.save(series);
        }

        return postRepository.save(post);
    }

    @Transactional
    public void remove(CustomUserDetails details, Long postId) {
        UserEntity author = userRepository.findByIdOrElseThrow(details.getUserId());
        PostEntity post = postRepository.findByIdOrElseThrow(postId);

        if (!post.isAuthor(author)) {
            throw new CustomException(ErrorType.AUTHORIZATION_ERROR, "작성자만 게시글을 삭제 할 수 있습니다.");
        }

        if (post.getSeries() != null) {
            SeriesEntity series = post.getSeries();
            series.setPostCount(series.getPostCount() - 1);
            seriesRepository.save(series);
            post.setSeries(null);
        }

        postRepository.deleteById(postId);
    }
}
