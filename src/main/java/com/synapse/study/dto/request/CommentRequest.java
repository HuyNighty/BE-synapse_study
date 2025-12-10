package com.synapse.study.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

public record CommentRequest(
        @NotBlank(message = "CONTENT_REQUIRED")
        String content,

        @NotNull(message = "POST_ID_REQUIRED")
        UUID postId,

        UUID parentId
) implements Serializable {}