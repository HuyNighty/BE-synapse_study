package com.synapse.study.service;

import com.synapse.study.dto.request.UserCreationRequest;
import com.synapse.study.dto.response.UserResponse;

public interface UserService {
    UserResponse createUser(UserCreationRequest request);
}
