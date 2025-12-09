package com.synapse.study.service;

import com.synapse.study.dto.request.UserCreationRequest;
import com.synapse.study.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(UserCreationRequest request);
    UserResponse getMyInfo();
    List<UserResponse> getUsers();
}
