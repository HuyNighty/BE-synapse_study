package com.synapse.study.dto.response;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public record CommentResponse(
        UUID id,
        String content,
        UserResponse user,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements Serializable {}