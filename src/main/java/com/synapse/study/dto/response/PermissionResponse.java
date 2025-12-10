package com.synapse.study.dto.response;

import java.io.Serializable;

public record PermissionResponse(
        String name,
        String description
) implements Serializable {}