package blog.jungmini.me.api;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import blog.jungmini.me.application.CommentService;
import blog.jungmini.me.common.response.ApiResponse;
import blog.jungmini.me.database.entity.CommentEntity;
import blog.jungmini.me.dto.request.CreateCommentRequest;
import blog.jungmini.me.dto.response.CreateCommentResponse;
import blog.jungmini.me.security.model.CustomUserDetails;

@RestController
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/v1/comments")
    public ApiResponse<CreateCommentResponse> create(
            Authentication authentication, @RequestBody @Valid CreateCommentRequest request) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        CommentEntity comment = commentService.create(customUserDetails.getUserId(), request.toEntity());

        return ApiResponse.success(CreateCommentResponse.fromEntity(comment));
    }
}
