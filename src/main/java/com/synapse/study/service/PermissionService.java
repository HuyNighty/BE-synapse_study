package com.synapse.study.service;

import com.synapse.study.dto.request.PermissionRequest;
import com.synapse.study.dto.response.PermissionResponse;

import java.util.List;
import java.util.Set;

public interface PermissionService {

    PermissionResponse create(PermissionRequest request);
    List<PermissionResponse> getAll();
    void delete(String permissionName);
    PermissionResponse update(String permissionName, PermissionRequest request);
    PermissionResponse getOne(String permissionName);
}
