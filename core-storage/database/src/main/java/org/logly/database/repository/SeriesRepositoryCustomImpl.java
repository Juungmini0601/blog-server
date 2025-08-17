package org.logly.database.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import org.logly.database.entity.QPostEntity;
import org.logly.database.entity.QSeriesEntity;
import org.logly.database.projection.SeriesItem;

import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class SeriesRepositoryCustomImpl implements SeriesRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public SeriesRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<SeriesItem> findSeriesItemsByUserId(Long userId) {
        QSeriesEntity series = QSeriesEntity.seriesEntity;
        QPostEntity post = QPostEntity.postEntity;

        return null;
        //        return queryFactory
        //                .select(Projections.fields(
        //                                SeriesItem.class,
        //                                series.seriesId,
        //                                series.name,
        //
        // JPAExpressions.select(post.count()).from(post).where(post.seriesId.eq(series.seriesId)))
        //                        .as("postCount"))
        //                .from(series)
        //                .fetchAll()
        //                .stream()
        //                .toList();
    }
}
