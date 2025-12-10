package com.synapse.study.service.impl;

import com.synapse.study.entity.Asset;
import com.synapse.study.repository.AssetRepository;
import com.synapse.study.service.AssetService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AssetServiceImpl implements AssetService {

    AssetRepository assetRepository;

    @NonFinal
    @Value("${app.upload-dir:./uploads}")
    String UPLOAD_DIR;

    @Transactional
    public Asset uploadFile(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file.");
            }

            String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            String extension = "";
            int dotIndex = originalFilename.lastIndexOf('.');
            if (dotIndex > 0) {
                extension = originalFilename.substring(dotIndex);
            }

            String newFileName = UUID.randomUUID().toString() + extension;

            Path targetLocation = Paths.get(UPLOAD_DIR).resolve(newFileName);
            Files.createDirectories(targetLocation.getParent());
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            Asset asset = Asset.builder()
                    .fileName(originalFilename)
                    .fileUrl("/uploads/" + newFileName)
                    .fileType(file.getContentType())
                    .fileSize(file.getSize())
                    .build();

            return assetRepository.save(asset);

        } catch (IOException e) {
            throw new RuntimeException("Could not store file " + file.getOriginalFilename(), e);
        }
    }
}