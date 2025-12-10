package com.synapse.study.dto.response;

import java.io.Serializable;

public record CategoryResponse(
        Long id,
        String name,
        String slug,
        String description
) implements Serializable {}