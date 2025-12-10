package com.synapse.study.service;

import com.synapse.study.entity.Asset;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface AssetService {

    Asset uploadFile(MultipartFile file);

    Asset getFile(UUID id);

    void deleteFile(UUID id);

    Page<Asset> getAll(Pageable pageable);

    Asset updateFile(UUID id, MultipartFile file);
}
