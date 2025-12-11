package com.synapse.study.dto.request;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

public record PostUpdateRequest(
        String title,
        String content,
        String summary,
        Long categoryId,
        UUID thumbnailId,
        Set<Long> tagIds
) implements Serializable {}