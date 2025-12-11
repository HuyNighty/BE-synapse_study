package com.synapse.study.controller;

import com.synapse.study.dto.request.ReactionRequest;
import com.synapse.study.dto.response.ApiResponse;
import com.synapse.study.dto.response.ReactionResponse;
import com.synapse.study.service.ReactionService;
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
@RequestMapping("/api/reactions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReactionController {

    ReactionService reactionService;

    @PostMapping
    @PreAuthorize("hasAuthority('REACTION_CREATE')")
    ApiResponse<ReactionResponse> react(@RequestBody @Valid ReactionRequest request) {
        return ApiResponse.<ReactionResponse>builder()
                .result(reactionService.react(request))
                .build();
    }

    @GetMapping("/posts/{postId}")
    ApiResponse<Page<ReactionResponse>> getReactions(
            @PathVariable UUID postId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());

        return ApiResponse.<Page<ReactionResponse>>builder()
                .result(reactionService.getReactionsByPost(postId, pageable))
                .build();
    }
}