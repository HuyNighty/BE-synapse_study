package com.synapse.study.controller;

import com.synapse.study.dto.request.RoleRequest;
import com.synapse.study.dto.response.ApiResponse;
import com.synapse.study.dto.response.PermissionResponse;
import com.synapse.study.dto.response.RoleResponse;
import com.synapse.study.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {

    RoleService roleService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CREATE')")
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.create(request))
                .build();
    }

    @GetMapping("/{roleName}/permissions")
    @PreAuthorize("hasAuthority('ROLE_READ')")
    ApiResponse<Set<PermissionResponse>> getPermissions(@PathVariable String roleName) {
        return ApiResponse.<Set<PermissionResponse>>builder()
                .result(roleService.getPermissionsByRole(roleName))
                .build();
    }
}
