package com.synapse.study.dto.response;

import com.synapse.study.entity.Asset;
import com.synapse.study.enums.PostStatus;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public record PostResponse(
        UUID id,
        String title,
        String slug,
        String summary,
        String content,
        PostStatus status,
        Asset thumbnail,
        CategoryResponse category,
        UserResponse user, // Tác giả
        LocalDateTime createdAt,
        Long viewCount
) implements Serializable {}