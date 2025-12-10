package com.synapse.study.controller;

import com.synapse.study.dto.response.ApiResponse;
import com.synapse.study.entity.Asset;
import com.synapse.study.service.AssetService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AssetController {

    AssetService assetService;

    @PreAuthorize("hasAuthority('ASSET_CREATE')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<Asset> upload(@RequestParam("file") MultipartFile file) {
        return ApiResponse.<Asset>builder()
                .result(assetService.uploadFile(file))
                .build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ASSET_DELETE')")
    ApiResponse<String> delete(@PathVariable UUID id) {
        assetService.deleteFile(id);
        return ApiResponse.<String>builder()
                .result("Asset deleted successfully")
                .build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    ApiResponse<Page<Asset>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());
        return ApiResponse.<Page<Asset>>builder()
                .result(assetService.getAll(pageable))
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<Asset> getFile(@PathVariable UUID id) {
        return ApiResponse.<Asset>builder()
                .result(assetService.getFile(id))
                .build();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ASSET_UPDATE')")
    ApiResponse<Asset> update(@PathVariable UUID id, @RequestParam("file") MultipartFile file) {
        return ApiResponse.<Asset>builder()
                .result(assetService.updateFile(id, file))
                .build();
    }
}