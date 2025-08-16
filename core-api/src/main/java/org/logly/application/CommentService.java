package org.logly.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.logly.database.entity.CommentEntity;
import org.logly.database.entity.UserEntity;
import org.logly.database.projection.CommentItem;
import org.logly.database.repository.CommentRepository;
import org.logly.database.repository.UserRepository;
import org.logly.error.CustomException;
import org.logly.error.ErrorType;
import org.logly.response.CursorResponse;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    private static final int PAGE_SIZE = 20;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public CommentEntity create(Long userId, CommentEntity commentEntity) {
        UserEntity user = userRepository.findByIdOrElseThrow(userId);
        commentEntity.setUser(user);

        return commentRepository.save(commentEntity);
    }

    @Transactional
    public CommentEntity update(Long requestUserId, CommentEntity commentEntity) {
        CommentEntity comment = commentRepository.findByIdOrElseThrow(commentEntity.getCommentId());

        if (!comment.isOwner(requestUserId)) {
            throw new CustomException(ErrorType.AUTHORIZATION_ERROR, "작성자만 수정 할 수 있습니다.");
        }

        comment.setContent(commentEntity.getContent());

        return commentRepository.save(comment);
    }

    @Transactional
    public void remove(Long userId, Long commentId) {
        CommentEntity comment = commentRepository.findByIdOrElseThrow(commentId);

        if (!comment.isOwner(userId)) {
            throw new CustomException(ErrorType.AUTHORIZATION_ERROR, "작성자만 삭제 할 수 있습니다.");
        }

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
