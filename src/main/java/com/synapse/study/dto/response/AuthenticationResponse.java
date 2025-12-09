package com.synapse.study.dto.response;

import java.io.Serializable;

public record AuthenticationResponse(
        String token,
        boolean authenticated
) implements Serializable {}