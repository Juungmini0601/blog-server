package org.logly.api;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import org.logly.application.CommentService;
import org.logly.database.entity.CommentEntity;
import org.logly.database.projection.CommentItem;
import org.logly.dto.request.CreateCommentRequest;
import org.logly.dto.request.UpdateCommentRequest;
import org.logly.dto.response.CreateCommentResponse;
import org.logly.dto.response.UpdateCommentResponse;
import org.logly.response.ApiResponse;
import org.logly.response.CursorResponse;
import org.logly.security.model.CustomUserDetails;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/v1/comments/{postId}")
    public CursorResponse<CommentItem, Long> getComments(
            @PathVariable Long postId, @RequestParam(required = false) Long lastCommentId) {
        if (lastCommentId == null) {
            lastCommentId = Long.MAX_VALUE;
        }

        return commentService.getComments(postId, lastCommentId);
    }

    @PostMapping("/v1/comments")
    public ApiResponse<CreateCommentResponse> create(
            Authentication authentication, @RequestBody @Valid CreateCommentRequest request) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        CommentEntity comment = commentService.create(customUserDetails.getUserId(), request.toEntity());

        return ApiResponse.success(CreateCommentResponse.fromEntity(comment));
    }

    @PutMapping("/v1/comments/{commentId}")
    public ApiResponse<UpdateCommentResponse> update(
            Authentication authentication,
            @PathVariable Long commentId,
            @RequestBody @Valid UpdateCommentRequest request) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        CommentEntity comment = commentService.update(customUserDetails.getUserId(), request.toEntity(commentId));

        return ApiResponse.success(UpdateCommentResponse.fromEntity(comment));
    }

    @DeleteMapping("/v1/comments/{commentId}")
    public ApiResponse<?> delete(Authentication authentication, @PathVariable Long commentId) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        commentService.remove(customUserDetails.getUserId(), commentId);

        return ApiResponse.success();
    }
}
