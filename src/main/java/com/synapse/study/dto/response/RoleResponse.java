package com.synapse.study.dto.response;

import java.io.Serializable;
import java.util.Set;

public record RoleResponse(
        String name,
        String description,
        Set<PermissionResponse> permissions
) implements Serializable {}