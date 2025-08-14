package blog.jungmini.me.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import blog.jungmini.me.common.error.CustomException;
import blog.jungmini.me.common.error.ErrorType;
import blog.jungmini.me.common.response.CursorResponse;
import blog.jungmini.me.database.entity.PostEntity;
import blog.jungmini.me.database.entity.SeriesEntity;
import blog.jungmini.me.database.entity.UserEntity;
import blog.jungmini.me.database.projection.PostDetail;
import blog.jungmini.me.database.projection.PostItem;
import blog.jungmini.me.database.repository.PostLikeRepository;
import blog.jungmini.me.database.repository.PostRepository;
import blog.jungmini.me.database.repository.SeriesRepository;
import blog.jungmini.me.database.repository.UserRepository;

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
