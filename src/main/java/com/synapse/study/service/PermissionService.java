package com.synapse.study.service;

import com.synapse.study.dto.request.PermissionRequest;
import com.synapse.study.dto.response.PermissionResponse;

import java.util.List;

public interface PermissionService {

    PermissionResponse create(PermissionRequest request);
    List<PermissionResponse> getAll();
}
