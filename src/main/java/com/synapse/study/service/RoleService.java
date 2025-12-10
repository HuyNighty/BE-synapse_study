package com.synapse.study.service;

import com.synapse.study.dto.request.RoleRequest;
import com.synapse.study.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {

    RoleResponse create(RoleRequest request);
    List<RoleResponse> getAll();
}
