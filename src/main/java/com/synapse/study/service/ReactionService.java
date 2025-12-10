package com.synapse.study.service;

import com.synapse.study.dto.request.ReactionRequest;
import com.synapse.study.dto.response.ReactionResponse;

public interface ReactionService {

    ReactionResponse react(ReactionRequest request);
}
