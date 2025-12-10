package com.synapse.study.controller;

import com.synapse.study.dto.response.ApiResponse;
import com.synapse.study.entity.Asset;
import com.synapse.study.service.AssetService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
}