package org.logly.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import org.logly.database.entity.CommentEntity;
import org.logly.database.entity.PostEntity;
import org.logly.database.entity.UserEntity;
import org.logly.database.projection.CommentItem;
import org.logly.database.repository.CommentRepository;
import org.logly.database.repository.PostRepository;
import org.logly.database.repository.UserRepository;
import org.logly.dto.request.CreateCommentRequest;
import org.logly.error.CustomException;
import org.logly.error.ErrorType;
import org.logly.response.CursorResponse;
import org.logly.security.model.CustomUserDetails;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    private static final int PAGE_SIZE = 20;

    @Transactional
    public CommentEntity create(CustomUserDetails details, CreateCommentRequest request) {
        UserEntity user = userRepository.findByIdOrElseThrow(details.getUserId());
        PostEntity post = postRepository.findByIdOrElseThrow(request.getPostId());
        // TODO 동시성 문제 증분 업데이트 추가 예정
        post.setCommentCount(post.getCommentCount() + 1);
        postRepository.save(post);

        CommentEntity comment = CommentEntity.builder()
                .content(request.getContent())
                .post(post)
                .user(user)
                .build();

        return commentRepository.save(comment);
    }

    @Transactional
    public CommentEntity update(CustomUserDetails details, CommentEntity commentEntity) {
        UserEntity requester = userRepository.findByIdOrElseThrow(details.getUserId());
        CommentEntity comment = commentRepository.findByIdOrElseThrow(commentEntity.getCommentId());

        if (!requester.equals(comment.getUser())) {
            throw new CustomException(ErrorType.AUTHORIZATION_ERROR, "작성자만 수정 할 수 있습니다.");
        }

        comment.setContent(commentEntity.getContent());

        return commentRepository.save(comment);
    }

    @Transactional
    public void remove(CustomUserDetails details, Long commentId) {
        UserEntity requester = userRepository.findByIdOrElseThrow(details.getUserId());
        CommentEntity comment = commentRepository.findByIdOrElseThrow(commentId);

        if (!requester.equals(comment.getUser())) {
            throw new CustomException(ErrorType.AUTHORIZATION_ERROR, "작성자만 삭제 할 수 있습니다.");
        }

        PostEntity post = comment.getPost();
        // TODO 동시성 문제 증분 업데이트 추가 예정
        post.setCommentCount(post.getCommentCount() - 1);
        commentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    public CursorResponse<CommentItem, Long> getComments(Long postId, Long lastCommentId) {
        List<CommentItem> comments = commentRepository.findCommentsByPostId(postId, lastCommentId);
        if (comments.isEmpty()) {
            return CursorResponse.of(comments, null, false);
        }

        Long nextCursor = comments.get(comments.size() - 1).commentId();
        boolean hasNext = comments.size() == PAGE_SIZE;
        return CursorResponse.of(comments, nextCursor, hasNext);
    }
}
