package com.synapse.study.controller;

import com.synapse.study.dto.request.ReactionRequest;
import com.synapse.study.dto.response.ApiResponse;
import com.synapse.study.dto.response.ReactionResponse;
import com.synapse.study.service.ReactionService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
}