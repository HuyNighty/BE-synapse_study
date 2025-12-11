package com.synapse.study.service;

import com.synapse.study.dto.request.RoleRequest;
import com.synapse.study.dto.response.PermissionResponse;
import com.synapse.study.dto.response.RoleResponse;
import com.synapse.study.entity.Permission;
import com.synapse.study.entity.Role;
import com.synapse.study.entity.RolePermission;
import com.synapse.study.enums.ErrorCode;
import com.synapse.study.exception.AppException;
import com.synapse.study.mapper.PermissionMapper;
import com.synapse.study.mapper.RoleMapper;
import com.synapse.study.repository.PermissionRepository;
import com.synapse.study.repository.RolePermissionRepository;
import com.synapse.study.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleServiceImpl implements RoleService {

    RoleRepository roleRepository;

    @Override
    public Set<PermissionResponse> getPermissionsByRole(String roleName) {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        return role.getRolePermissions().stream()
                .map(rp -> permissionMapper.toPermissionResponse(rp.getPermission()))
                .collect(Collectors.toSet());
    }

    PermissionRepository permissionRepository;
    RolePermissionRepository rolePermissionRepository;
    RoleMapper roleMapper;
    PermissionMapper permissionMapper;

    @Override
    @Transactional
    public RoleResponse create(RoleRequest request) {
        var role = roleMapper.toRole(request);
        role = roleRepository.save(role);

        var permissions = permissionRepository.findAllByNameIn(request.permissions());

        Set<RolePermission> rolePermissions = new HashSet<>();
        for (Permission permission : permissions) {
            RolePermission rp = RolePermission.builder()
                    .id(new RolePermission.RolePermissionKey(role.getId(), permission.getId()))
                    .role(role)
                    .permission(permission)
                    .build();
            rolePermissions.add(rp);
        }
        rolePermissionRepository.saveAll(rolePermissions);
        role.setRolePermissions(rolePermissions);

        return toResponse(role);
    }

    public List<RoleResponse> getAll() {
        return roleRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    private RoleResponse toResponse(Role role) {
        var response = roleMapper.toRoleResponse(role);

        var permissions = role.getRolePermissions().stream()
                .map(rp -> permissionMapper.toPermissionResponse(rp.getPermission()))
                .collect(Collectors.toSet());

        return new RoleResponse(response.name(), response.description(), permissions);
    }
}