package org.logly.api;

import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import org.logly.application.PostDraftService;
import org.logly.database.entity.PostDraftEntity;
import org.logly.database.projection.PostDraftItem;
import org.logly.dto.request.CreatePostDraftRequest;
import org.logly.dto.request.UpdatePostDraftRequest;
import org.logly.dto.response.CreatePostDraftResponse;
import org.logly.dto.response.UpdatePostDraftResponse;
import org.logly.response.ApiResponse;
import org.logly.response.CursorResponse;
import org.logly.security.model.CustomUserDetails;

@RestController
@RequiredArgsConstructor
public class PostDraftController {

    private final PostDraftService postDraftService;

    @PostMapping("/v1/postdrafts")
    public ApiResponse<CreatePostDraftResponse> create(
            Authentication authentication, @RequestBody @Validated CreatePostDraftRequest request) {
        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
        PostDraftEntity postDraft = postDraftService.save(details, request);

        return ApiResponse.success(CreatePostDraftResponse.fromEntity(postDraft));
    }

    @PutMapping("/v1/postdrafts/{postdraftId}")
    public ApiResponse<?> update(
            Authentication authentication,
            @PathVariable Long postdraftId,
            @RequestBody UpdatePostDraftRequest request) {
        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
        PostDraftEntity updatedDraft = postDraftService.update(details, postdraftId, request);
        return ApiResponse.success(UpdatePostDraftResponse.fromEntity(updatedDraft));
    }

    @GetMapping("/v1/postdrafts")
    public CursorResponse<PostDraftItem, Long> getPostDrafts(
            Authentication authentication, @RequestParam(required = false) Long lastPostDraftId) {
        if (lastPostDraftId == null) {
            lastPostDraftId = Long.MAX_VALUE;
        }

        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
        return postDraftService.getPostDraftList(details, lastPostDraftId);
    }

    @DeleteMapping("/v1/postdrafts/{postdraftId}")
    public ApiResponse<?> delete(Authentication authentication, @PathVariable Long postdraftId) {
        CustomUserDetails details = (CustomUserDetails) authentication.getPrincipal();
        postDraftService.remove(details, postdraftId);

        return ApiResponse.success();
    }
}
