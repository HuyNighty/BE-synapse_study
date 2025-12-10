package com.synapse.study.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

public record PostCreationRequest(
        @NotBlank(message = "TITLE_REQUIRED")
        String title,

        @NotBlank(message = "CONTENT_REQUIRED")
        String content,

        String summary,

        @NotNull(message = "CATEGORY_REQUIRED")
        Long categoryId,

        UUID thumbnailId,

        Set<Long> tagIds
) implements Serializable {}