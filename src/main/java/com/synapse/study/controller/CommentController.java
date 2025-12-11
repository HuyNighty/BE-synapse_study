package com.synapse.study.controller;

import com.synapse.study.dto.request.CommentRequest;
import com.synapse.study.dto.response.ApiResponse;
import com.synapse.study.dto.response.CommentResponse;
import com.synapse.study.service.CommentService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentController {

    CommentService commentService;

    @PostMapping
    @PreAuthorize("hasAuthority('COMMENT_CREATE')")
    ApiResponse<CommentResponse> create(@RequestBody @Valid CommentRequest request) {
        return ApiResponse.<CommentResponse>builder()
                .result(commentService.createComment(request))
                .build();
    }

    @GetMapping("/post/{postId}")
    ApiResponse<List<CommentResponse>> getComments(
            @PathVariable UUID postId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<List<CommentResponse>>builder()
                .result(commentService.getCommentsByPost(postId, page, size))
                .build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('COMMENT_UPDATE')")
    ApiResponse<CommentResponse> update(@PathVariable UUID id, @RequestBody CommentRequest request) {
        return ApiResponse.<CommentResponse>builder()
                .result(commentService.updateComment(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('COMMENT_DELETE')")
    ApiResponse<String> delete(@PathVariable UUID id) {
        commentService.deleteComment(id);
        return ApiResponse.<String>builder()
                .result("Comment deleted successfully")
                .build();
    }

    @GetMapping("/{commentId}/replies")
    ApiResponse<List<CommentResponse>> getReplies(
            @PathVariable UUID commentId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ApiResponse.<List<CommentResponse>>builder()
                .result(commentService.getReplies(commentId, page, size))
                .build();
    }
}