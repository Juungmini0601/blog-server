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

        return queryFactory
                .select(createPostItemProjection(post))
                .from(post)
                .join(post.user)
                .where(post.postId.lt(lastPostId), post.isPublic.isTrue())
                .orderBy(post.postId.desc())
                .limit(20)
                .fetch();
    }

    @Override
    public List<PostItem> findPostItemsByUser(UserEntity user, Long lastPostId) {
        QPostEntity post = QPostEntity.postEntity;

        return queryFactory
                .select(createPostItemProjection(post))
                .from(post)
                .join(post.user)
                .where(post.postId.lt(lastPostId), post.isPublic.isTrue(), post.user.eq(user))
                .orderBy(post.postId.desc())
                .limit(20)
                .fetch();
    }

    @Override
    public List<PostItem> findPostItemsBySeries(SeriesEntity series, Long lastPostId) {
        QPostEntity post = QPostEntity.postEntity;

        return queryFactory
                .select(createPostItemProjection(post))
                .from(post)
                .join(post.user)
                .where(post.postId.lt(lastPostId), post.isPublic.isTrue(), post.series.eq(series))
                .orderBy(post.postId.desc())
                .limit(20)
                .fetch();
    }

    private static QBean<PostItem> createPostItemProjection(QPostEntity post) {
        return Projections.fields(
                PostItem.class,
                post.postId,
                post.thumbnailUrl,
                post.createdAt,
                post.content,
                post.user.userId,
                post.user.nickname,
                post.user.profileImageUrl,
                post.commentCount,
                post.likeCount);
    }
}
