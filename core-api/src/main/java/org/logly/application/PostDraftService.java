package org.logly.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import org.logly.database.entity.PostDraftEntity;
import org.logly.database.entity.SeriesEntity;
import org.logly.database.entity.UserEntity;
import org.logly.database.projection.PostDraftItem;
import org.logly.database.repository.PostDraftRepository;
import org.logly.database.repository.SeriesRepository;
import org.logly.database.repository.UserRepository;
import org.logly.dto.request.CreatePostDraftRequest;
import org.logly.dto.request.UpdatePostDraftRequest;
import org.logly.error.CustomException;
import org.logly.error.ErrorType;
import org.logly.response.CursorResponse;
import org.logly.security.model.CustomUserDetails;

@Service
@RequiredArgsConstructor
public class PostDraftService {
    private final PostDraftRepository postDraftRepository;
    private final UserRepository userRepository;
    private final SeriesRepository seriesRepository;

    private static final int PAGE_SIZE = 20;

    @Transactional(readOnly = true)
    public CursorResponse<PostDraftItem, Long> getPostDraftList(CustomUserDetails details, Long lastPostDraftId) {
        UserEntity user = userRepository.findByIdOrElseThrow(details.getUserId());
        List<PostDraftItem> postDrafts = postDraftRepository.findPostDraftsByUser(user, lastPostDraftId);

        if (postDrafts.isEmpty()) {
            return CursorResponse.of(postDrafts, null, false);
        }

        Long nextCursor = postDrafts.get(postDrafts.size() - 1).getPostDraftId();
        boolean hasNext = postDrafts.size() == PAGE_SIZE;
        return CursorResponse.of(postDrafts, nextCursor, hasNext);
    }

    @Transactional
    public PostDraftEntity save(CustomUserDetails details, CreatePostDraftRequest request) {
        UserEntity author = userRepository.findByIdOrElseThrow(details.getUserId());
        PostDraftEntity postDraft = PostDraftEntity.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .thumbnailUrl(request.getThumbnailUrl())
                .isPublic(request.isPublic())
                .user(author)
                .build();

        if (request.getSeriesId() != null) {
            SeriesEntity series = seriesRepository.findByIdOrElseThrow(request.getSeriesId());

            if (!series.getUser().equals(author)) {
                throw new CustomException(ErrorType.VALIDATION_ERROR, "다른 사람의 시리즈 입니다.");
            }

            postDraft.setSeries(series);
            seriesRepository.save(series);
        }

        return postDraftRepository.save(postDraft);
    }

    @Transactional
    public PostDraftEntity update(CustomUserDetails details, Long postDraftId, UpdatePostDraftRequest request) {
        UserEntity author = userRepository.findByIdOrElseThrow(details.getUserId());
        PostDraftEntity postDraft = postDraftRepository.findByIdOrElseThrow(postDraftId);

        if (!postDraft.getUser().equals(author)) {
            throw new CustomException(ErrorType.AUTHORIZATION_ERROR, "작성자만 초안을 수정 할 수 있습니다.");
        }

        postDraft.setTitle(request.getTitle());
        postDraft.setContent(request.getContent());
        postDraft.setThumbnailUrl(request.getThumbnailUrl());
        postDraft.setIsPublic(request.isPublic());

        if (request.getSeriesId() != null) {
            SeriesEntity series = seriesRepository.findByIdOrElseThrow(request.getSeriesId());

            if (!series.getUser().equals(author)) {
                throw new CustomException(ErrorType.VALIDATION_ERROR, "다른 사람의 시리즈 입니다.");
            }

            postDraft.setSeries(series);
        } else {
            postDraft.setSeries(null);
        }

        return postDraftRepository.save(postDraft);
    }

    @Transactional
    public void remove(CustomUserDetails details, Long postDraftId) {
        UserEntity author = userRepository.findByIdOrElseThrow(details.getUserId());
        PostDraftEntity postDraft = postDraftRepository.findByIdOrElseThrow(postDraftId);

        if (!postDraft.getUser().equals(author)) {
            throw new CustomException(ErrorType.AUTHORIZATION_ERROR, "작성자만 초안을 삭제 할 수 있습니다.");
        }

        postDraftRepository.deleteById(postDraftId);
    }
}
