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
}