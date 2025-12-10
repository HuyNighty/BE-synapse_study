package com.synapse.study.controller;

import com.synapse.study.dto.response.ApiResponse;
import com.synapse.study.service.BookmarkService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookmarkController {

    BookmarkService bookmarkService;

    @PostMapping("/{postId}")
    @PreAuthorize("hasAuthority('BOOKMARK_CREATE')")
    ApiResponse<String> toggle(@PathVariable UUID postId) {
        return ApiResponse.<String>builder()
                .result(bookmarkService.toggleBookmark(postId))
                .build();
    }
}