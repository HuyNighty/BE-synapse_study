package com.synapse.study.dto.request;

import java.io.Serializable;

public record AuthenticationRequest(
        String identifier,
        String password
) implements Serializable {}