package com.synapse.study.service;

import com.synapse.study.dto.request.RoleRequest;
import com.synapse.study.dto.response.PermissionResponse;
import com.synapse.study.dto.response.RoleResponse;

import java.util.List;
import java.util.Set;

public interface RoleService {

    RoleResponse create(RoleRequest request);
    List<RoleResponse> getAll();
    Set<PermissionResponse> getPermissionsByRole(String roleName);
}
