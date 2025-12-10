package com.synapse.study.mapper;

import com.synapse.study.dto.request.PermissionRequest;
import com.synapse.study.dto.response.PermissionResponse;
import com.synapse.study.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}