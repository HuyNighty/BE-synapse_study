package com.synapse.study.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public record UserCreationRequest(

        @Size(min = 3, message = "USERNAME_INVALID")
        String username,

        @Size(min = 8, message = "INVALID_PASSWORD")
        String password,

        @Email(message = "INVALID_EMAIL")
        String email,

        String firstName,
        String lastName
) implements Serializable {}