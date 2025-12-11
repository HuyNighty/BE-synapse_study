package com.synapse.study.controller;

import com.synapse.study.dto.request.PostCreationRequest;
import com.synapse.study.dto.request.PostUpdateRequest;
import com.synapse.study.dto.response.ApiResponse;
import com.synapse.study.dto.response.PostResponse;
import com.synapse.study.service.PostService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
    ApiResponse<Page<PostResponse>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        return ApiResponse.<Page<PostResponse>>builder()
                .result(postService.getAllPosts(pageable))
                .build();
    }

    @GetMapping("/{slug}")
    ApiResponse<PostResponse> getPostBySlug(@PathVariable String slug) {
        return ApiResponse.<PostResponse>builder()
                .result(postService.getPostBySlug(slug))
                .build();
    }

    @GetMapping("/my-posts")
    ApiResponse<Page<PostResponse>> getMyPosts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        return ApiResponse.<Page<PostResponse>>builder()
                .result(postService.getMyPosts(pageable))
                .build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('POST_UPDATE')")
    ApiResponse<PostResponse> update(@PathVariable UUID id, @RequestBody PostUpdateRequest request) {
        return ApiResponse.<PostResponse>builder()
                .result(postService.updatePost(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('POST_DELETE')")
    ApiResponse<String> delete(@PathVariable UUID id) {
        postService.deletePost(id);
        return ApiResponse.<String>builder().result("Post deleted successfully").build();
    }
}