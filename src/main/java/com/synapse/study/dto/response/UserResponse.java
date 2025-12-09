package com.synapse.study.dto.response;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String email,
        String firstName,
        String lastName,
        Boolean isActive,
        Set<String> roles
) implements Serializable {}