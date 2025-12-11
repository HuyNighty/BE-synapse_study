package com.synapse.study.service;

import com.synapse.study.dto.request.ReactionRequest;
import com.synapse.study.dto.response.ReactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ReactionService {

    ReactionResponse react(ReactionRequest request);
    Page<ReactionResponse> getReactionsByPost(UUID postId, Pageable pageable);
}
