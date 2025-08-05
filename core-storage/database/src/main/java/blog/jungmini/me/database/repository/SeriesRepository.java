package blog.jungmini.me.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import blog.jungmini.me.database.entity.SeriesEntity;

@Repository
public interface SeriesRepository extends JpaRepository<SeriesEntity, Long> {}
