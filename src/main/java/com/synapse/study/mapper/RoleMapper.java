package com.synapse.study.mapper;

import com.synapse.study.dto.request.RoleRequest;
import com.synapse.study.dto.response.RoleResponse;
import com.synapse.study.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "rolePermissions", ignore = true)
    Role toRole(RoleRequest request);

    @Mapping(target = "permissions", ignore = true)
    RoleResponse toRoleResponse(Role role);
}