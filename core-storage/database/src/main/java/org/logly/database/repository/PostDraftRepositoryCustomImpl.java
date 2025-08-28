package org.logly.database.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

import org.logly.database.entity.QPostDraftEntity;
import org.logly.database.entity.QSeriesEntity;
import org.logly.database.entity.QUserEntity;
import org.logly.database.entity.UserEntity;
import org.logly.database.projection.PostDraftItem;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
@RequiredArgsConstructor
public class PostDraftRepositoryCustomImpl implements PostDraftRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PostDraftItem> findPostDraftsByUser(UserEntity user, Long lastPostDraftId) {
        QPostDraftEntity postDraft = QPostDraftEntity.postDraftEntity;
        QUserEntity userEntity = QUserEntity.userEntity;
        QSeriesEntity series = QSeriesEntity.seriesEntity;

        return queryFactory
                .select(createPostDraftItemProjection(postDraft, userEntity, series))
                .from(postDraft)
                .join(postDraft.user, userEntity)
                .leftJoin(postDraft.series, series)
                .where(postDraft.user.eq(user), postDraft.postDraftId.lt(lastPostDraftId))
                .orderBy(postDraft.postDraftId.desc())
                .limit(20)
                .fetch();
    }

    private static QBean<PostDraftItem> createPostDraftItemProjection(
            QPostDraftEntity postDraft, QUserEntity user, QSeriesEntity series) {
        return Projections.fields(
                PostDraftItem.class,
                postDraft.postDraftId,
                postDraft.title,
                postDraft.content,
                postDraft.thumbnailUrl,
                postDraft.isPublic,
                postDraft.createdAt,
                postDraft.updatedAt,
                user.userId,
                user.nickname,
                user.profileImageUrl,
                series.seriesId,
                series.name.as("seriesName"));
    }
}
