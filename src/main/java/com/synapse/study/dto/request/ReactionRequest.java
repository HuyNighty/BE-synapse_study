package com.synapse.study.dto.request;

import com.synapse.study.enums.ReactionType;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

public record ReactionRequest(
        @NotNull(message = "POST_ID_REQUIRED")
        UUID postId,

        @NotNull(message = "TYPE_REQUIRED")
        ReactionType type
) implements Serializable {}