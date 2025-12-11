package com.synapse.study.controller;

import com.synapse.study.dto.request.PermissionRequest;
import com.synapse.study.dto.response.ApiResponse;
import com.synapse.study.dto.response.PermissionResponse;
import com.synapse.study.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {

    PermissionService permissionService;

    @PostMapping
    @PreAuthorize("hasAuthority('PERMISSION_CREATE')")
    ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.create(request))
                .build();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PERMISSION_READ')")
    ApiResponse<List<PermissionResponse>> getAll() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .result(permissionService.getAll())
                .build();
    }

    @DeleteMapping("/{permissionName}")
    @PreAuthorize("hasAuthority('PERMISSION_DELETE')")
    ApiResponse<String> delete(@PathVariable String permissionName) {
        permissionService.delete(permissionName);
        return ApiResponse.<String>builder().result("Permission deleted").build();
    }

    @PatchMapping("/{permissionName}")
    @PreAuthorize("hasAuthority('PERMISSION_UPDATE')")
    ApiResponse<PermissionResponse> update(@PathVariable String permissionName, @RequestBody PermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.update(permissionName, request))
                .build();
    }

    @GetMapping("/{permissionName}")
    @PreAuthorize("hasAuthority('PERMISSION_READ')")
    ApiResponse<PermissionResponse> getOne(@PathVariable String permissionName) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.getOne(permissionName))
                .build();
    }
}