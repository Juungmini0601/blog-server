package blog.jungmini.me.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import blog.jungmini.me.database.entity.PostEntity;
import blog.jungmini.me.database.entity.SeriesEntity;
import blog.jungmini.me.database.entity.UserEntity;
import blog.jungmini.me.database.repository.PostRepository;
import blog.jungmini.me.database.repository.SeriesRepository;
import blog.jungmini.me.database.repository.UserRepository;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final SeriesRepository seriesRepository;

    public PostService(
            PostRepository postRepository, UserRepository userRepository, SeriesRepository seriesRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.seriesRepository = seriesRepository;
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
}
