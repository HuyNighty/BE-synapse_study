package com.synapse.study.controller;

import com.synapse.study.dto.request.PostCreationRequest;
import com.synapse.study.dto.response.ApiResponse;
import com.synapse.study.dto.response.PostResponse;
import com.synapse.study.service.PostService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostController {

    PostService postService;

    @PostMapping
    @PreAuthorize("hasAuthority('POST_CREATE')")
    ApiResponse<PostResponse> create(@RequestBody @Valid PostCreationRequest request) {
        return ApiResponse.<PostResponse>builder()
                .result(postService.createPost(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<PostResponse>> getAll() {
        return ApiResponse.<List<PostResponse>>builder()
                .result(postService.getAllPosts())
                .build();
    }

    @GetMapping("/{slug}")
    ApiResponse<PostResponse> getPostBySlug(@PathVariable String slug) {
        return ApiResponse.<PostResponse>builder()
                .result(postService.getPostBySlug(slug))
                .build();
    }
}