package blog.jungmini.me.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
