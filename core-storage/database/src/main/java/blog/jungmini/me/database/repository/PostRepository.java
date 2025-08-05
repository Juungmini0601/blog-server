package blog.jungmini.me.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import blog.jungmini.me.database.entity.PostEntity;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE PostEntity p SET p.seriesId = NULL WHERE p.seriesId = :seriesId")
    void setSeriesIdNullBySeriesId(Long seriesId);
}
