package org.logly.database.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import org.logly.database.entity.*;
import org.logly.database.projection.PostItem;

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
    public List<PostItem> findPostItemsByUserId(Long userId, Long lastPostId) {
        QPostEntity post = QPostEntity.postEntity;
        QUserEntity user = QUserEntity.userEntity;
        QPostLikeEntity like = QPostLikeEntity.postLikeEntity;
        QCommentEntity comment = QCommentEntity.commentEntity;
        return null;

        //        return queryFactory
        //                .select(Projections.fields(
        //                        PostItem.class,
        //                        post.postId.as("postId"),
        //                        post.thumbnailUrl.as("thumbnailUrl"),
        //                        post.createdAt.as("createdAt"),
        //                        post.content.as("content"),
        //                        post.userId.as("userId"),
        //                        user.nickname.as("nickname"),
        //                        user.profileImageUrl.as("profileImageUrl"),
        //
        // JPAExpressions.select(comment.count()).from(comment).where(comment.postId.eq(post.postId)),
        //                        JPAExpressions.select(like.count()).from(like).where(like.postId.eq(post.postId))))
        //                .from(post)
        //                .join(user)
        //                .on(post.userId.eq(user.userId))
        //                .where(post.userId.eq(userId).and(post.postId.lt(lastPostId)), post.isPublic.isTrue())
        //                .orderBy(post.postId.desc())
        //                .limit(20)
        //                .fetch();
    }

    @Override
    public List<PostItem> findPostItemsBySeriesId(Long seriesId, Long lastPostId) {
        QPostEntity post = QPostEntity.postEntity;
        QUserEntity user = QUserEntity.userEntity;
        QPostLikeEntity like = QPostLikeEntity.postLikeEntity;
        QCommentEntity comment = QCommentEntity.commentEntity;
        return null;
        //        return queryFactory
        //                .select(Projections.fields(
        //                        PostItem.class,
        //                        post.postId.as("postId"),
        //                        post.thumbnailUrl.as("thumbnailUrl"),
        //                        post.createdAt.as("createdAt"),
        //                        post.content.as("content"),
        //                        post.userId.as("userId"),
        //                        user.nickname.as("nickname"),
        //                        user.profileImageUrl.as("profileImageUrl"),
        //
        // JPAExpressions.select(comment.count()).from(comment).where(comment.postId.eq(post.postId)),
        //                        JPAExpressions.select(like.count()).from(like).where(like.postId.eq(post.postId))))
        //                .from(post)
        //                .join(user)
        //                .on(post.userId.eq(user.userId))
        //                .where(post.seriesId.eq(seriesId).and(post.postId.lt(lastPostId)), post.isPublic.isTrue())
        //                .orderBy(post.postId.desc())
        //                .limit(20)
        //                .fetch();
    }

    @Override
    public List<PostItem> findPosts(Long lastPostId) {
        QPostEntity post = QPostEntity.postEntity;
        QUserEntity user = QUserEntity.userEntity;
        QPostLikeEntity like = QPostLikeEntity.postLikeEntity;
        QCommentEntity comment = QCommentEntity.commentEntity;
        return null;
        //        return queryFactory
        //                .select(Projections.fields(
        //                        PostItem.class,
        //                        post.postId.as("postId"),
        //                        post.thumbnailUrl.as("thumbnailUrl"),
        //                        post.createdAt.as("createdAt"),
        //                        post.content.as("content"),
        //                        post.userId.as("userId"),
        //                        user.nickname.as("nickname"),
        //                        user.profileImageUrl.as("profileImageUrl"),
        //
        // JPAExpressions.select(comment.count()).from(comment).where(comment.postId.eq(post.postId)),
        //                        JPAExpressions.select(like.count()).from(like).where(like.postId.eq(post.postId))))
        //                .from(post)
        //                .join(user)
        //                .on(post.userId.eq(user.userId))
        //                .where((post.postId.lt(lastPostId)), post.isPublic.isTrue())
        //                .orderBy(post.postId.desc())
        //                .limit(20)
        //                .fetch();
    }
}
