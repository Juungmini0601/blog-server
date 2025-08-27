package org.logly.database.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import org.logly.database.entity.*;
import org.logly.database.projection.PostItem;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PostRepositoryCustomImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<PostItem> searchPosts(String keyword, Long lastPostId) {
        QPostEntity post = QPostEntity.postEntity;
        QPostStatisticsEntity statistics = QPostStatisticsEntity.postStatisticsEntity;

        return queryFactory
                .select(createPostItemProjection(post, statistics))
                .from(post)
                .join(post.user)
                .join(post.statistics, statistics)
                .where(
                        post.postId.lt(lastPostId),
                        post.isPublic.isTrue(),
                        post.content.contains(keyword).or(post.title.contains(keyword)))
                .orderBy(post.postId.desc())
                .limit(20)
                .fetch();
    }

    @Override
    public void setSeriesIdNullBySeries(SeriesEntity series) {
        QPostEntity post = QPostEntity.postEntity;
        queryFactory
                .update(post)
                .setNull(post.series)
                .where(post.series.eq(series))
                .execute();
    }

    @Override
    public List<PostItem> findPosts(Long lastPostId) {
        QPostEntity post = QPostEntity.postEntity;
        QPostStatisticsEntity statistics = QPostStatisticsEntity.postStatisticsEntity;

        return queryFactory
                .select(createPostItemProjection(post, statistics))
                .from(post)
                .join(post.user)
                .join(post.statistics, statistics)
                .where(post.postId.lt(lastPostId), post.isPublic.isTrue())
                .orderBy(post.postId.desc())
                .limit(20)
                .fetch();
    }

    @Override
    public List<PostItem> findPostItemsByUser(UserEntity user, Long lastPostId) {
        QPostEntity post = QPostEntity.postEntity;
        QPostStatisticsEntity statistics = QPostStatisticsEntity.postStatisticsEntity;

        return queryFactory
                .select(createPostItemProjection(post, statistics))
                .from(post)
                .join(post.user)
                .join(post.statistics, statistics)
                .where(post.postId.lt(lastPostId), post.isPublic.isTrue(), post.user.eq(user))
                .orderBy(post.postId.desc())
                .limit(20)
                .fetch();
    }

    @Override
    public List<PostItem> findPostItemsBySeries(SeriesEntity series, Long lastPostId) {
        QPostEntity post = QPostEntity.postEntity;
        QPostStatisticsEntity statistics = QPostStatisticsEntity.postStatisticsEntity;

        return queryFactory
                .select(createPostItemProjection(post, statistics))
                .from(post)
                .join(post.user)
                .join(post.statistics, statistics)
                .where(post.postId.lt(lastPostId), post.isPublic.isTrue(), post.series.eq(series))
                .orderBy(post.postId.desc())
                .limit(20)
                .fetch();
    }

    @Override
    public void incrementCommentCount(Long postId) {
        QPostStatisticsEntity statistics = QPostStatisticsEntity.postStatisticsEntity;

        queryFactory
                .update(statistics)
                .set(statistics.commentCount, statistics.commentCount.add(1))
                .where(statistics.postId.eq(postId))
                .execute();
    }

    @Override
    public void decrementCommentCount(Long postId) {
        QPostStatisticsEntity statistics = QPostStatisticsEntity.postStatisticsEntity;

        queryFactory
                .update(statistics)
                .set(statistics.commentCount, statistics.commentCount.subtract(1))
                .where(statistics.postId.eq(postId))
                .execute();
    }

    @Override
    public void incrementViewCount(Long postId) {
        QPostStatisticsEntity statistics = QPostStatisticsEntity.postStatisticsEntity;

        queryFactory
                .update(statistics)
                .set(statistics.viewCount, statistics.viewCount.add(1))
                .where(statistics.postId.eq(postId))
                .execute();
    }

    @Override
    public void decrementViewCount(Long postId) {
        QPostStatisticsEntity statistics = QPostStatisticsEntity.postStatisticsEntity;

        queryFactory
                .update(statistics)
                .set(statistics.viewCount, statistics.viewCount.subtract(1))
                .where(statistics.postId.eq(postId))
                .execute();
    }

    @Override
    public void incrementLikeCount(Long postId) {
        QPostStatisticsEntity statistics = QPostStatisticsEntity.postStatisticsEntity;

        queryFactory
                .update(statistics)
                .set(statistics.likeCount, statistics.likeCount.add(1))
                .where(statistics.postId.eq(postId))
                .execute();
    }

    @Override
    public void decrementLikeCount(Long postId) {
        QPostStatisticsEntity statistics = QPostStatisticsEntity.postStatisticsEntity;

        queryFactory
                .update(statistics)
                .set(statistics.likeCount, statistics.likeCount.subtract(1))
                .where(statistics.postId.eq(postId))
                .execute();
    }

    private static QBean<PostItem> createPostItemProjection(QPostEntity post, QPostStatisticsEntity statistics) {
        return Projections.fields(
                PostItem.class,
                post.postId,
                post.thumbnailUrl,
                post.title,
                post.createdAt,
                post.content,
                post.user.userId,
                post.user.nickname,
                post.user.profileImageUrl,
                statistics.commentCount,
                statistics.likeCount);
    }
}
