package com.synapse.study.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

public record PermissionRequest(
        @NotBlank(message = "FIELD_REQUIRED")
        String name,
        String description
) implements Serializable {}