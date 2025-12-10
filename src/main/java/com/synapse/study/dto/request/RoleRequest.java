package com.synapse.study.dto.request;

import java.io.Serializable;
import java.util.Set;

public record RoleRequest(
        String name,
        String description,
        Set<String> permissions
) implements Serializable {}