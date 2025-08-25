package org.logly.database.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

import org.logly.database.entity.QCommentEntity;
import org.logly.database.entity.QUserEntity;
import org.logly.database.projection.CommentItem;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<CommentItem> findCommentsByPostId(Long postId, Long lastCommentId) {
        QCommentEntity comment = QCommentEntity.commentEntity;
        QUserEntity user = QUserEntity.userEntity;

        return queryFactory
                .select(createCommentItemProjection(comment, user))
                .from(comment)
                .join(comment.user, user)
                .where(comment.post.postId.eq(postId), comment.commentId.lt(lastCommentId))
                .orderBy(comment.commentId.desc())
                .limit(20)
                .fetch();
    }

    private static ConstructorExpression<CommentItem> createCommentItemProjection(
            QCommentEntity comment, QUserEntity user) {
        return Projections.constructor(
                CommentItem.class,
                comment.commentId,
                comment.post.postId,
                comment.content,
                comment.createdAt,
                comment.user.userId,
                user.nickname,
                user.profileImageUrl);
    }
}
