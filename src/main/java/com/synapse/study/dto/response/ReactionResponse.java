package com.synapse.study.dto.response;

import com.synapse.study.enums.ReactionType;
import java.io.Serializable;
import java.util.UUID;

public record ReactionResponse(
        Long id,
        UUID postId,
        ReactionType type,
        UserResponse user,
        Long totalReactions
) implements Serializable {}