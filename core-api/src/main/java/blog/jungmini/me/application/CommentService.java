package blog.jungmini.me.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import blog.jungmini.me.common.error.CustomException;
import blog.jungmini.me.common.error.ErrorType;
import blog.jungmini.me.database.entity.CommentEntity;
import blog.jungmini.me.database.entity.UserEntity;
import blog.jungmini.me.database.repository.CommentRepository;
import blog.jungmini.me.database.repository.UserRepository;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

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
}
