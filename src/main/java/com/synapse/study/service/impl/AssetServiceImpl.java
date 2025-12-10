package com.synapse.study.service.impl;

import com.synapse.study.entity.Asset;
import com.synapse.study.entity.Post;
import com.synapse.study.entity.User;
import com.synapse.study.enums.ErrorCode;
import com.synapse.study.exception.AppException;
import com.synapse.study.repository.AssetRepository;
import com.synapse.study.repository.PostRepository;
import com.synapse.study.repository.UserRepository;
import com.synapse.study.service.AssetService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AssetServiceImpl implements AssetService {

    AssetRepository assetRepository;
    UserRepository userRepository;
    PostRepository postRepository;

    @NonFinal
    @Value("${app.upload-dir:./uploads}")
    String UPLOAD_DIR;

    @Override
    @Transactional
    public Asset uploadFile(MultipartFile file) {
        String newFileName = storeFileToDisk(file);


        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        var context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();
        User currentUser = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Asset asset = Asset.builder()
                .fileName(originalFilename)
                .fileUrl("/uploads/" + newFileName)
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .user(currentUser)
                .build();

        return assetRepository.save(asset);
    }

    @Override
    @Transactional
    public void deleteFile(UUID id) {
        Asset asset = getFile(id);

        checkOwnership(asset);
        List<Post> posts = postRepository.findByThumbnail(asset);
        if (!posts.isEmpty()) {
            for (Post post : posts) {
                post.setThumbnail(null);
                postRepository.save(post);
            }
        }
        deleteFileFromDisk(asset.getFileUrl());

        assetRepository.delete(asset);
    }

    @Override
    @Transactional
    public Asset updateFile(UUID id, MultipartFile file) {
        Asset asset = getFile(id);

        checkOwnership(asset);

        String newFileName = storeFileToDisk(file);

        deleteFileFromDisk(asset.getFileUrl());

        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        asset.setFileName(originalFilename);
        asset.setFileUrl("/uploads/" + newFileName);
        asset.setFileType(file.getContentType());
        asset.setFileSize(file.getSize());

        return assetRepository.save(asset);
    }

    @Override
    public Asset getFile(UUID id) {
        return assetRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ASSET_NOT_FOUND));
    }

    @Override
    public Page<Asset> getAll(Pageable pageable) {
        return assetRepository.findAll(pageable);
    }

    private void checkOwnership(Asset asset) {
        var context = SecurityContextHolder.getContext();
        String currentUserId = context.getAuthentication().getName();
        var authorities = context.getAuthentication().getAuthorities();

        boolean isAdmin = authorities.stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        boolean isOwner = asset.getUser() != null &&
                asset.getUser().getId().toString().equals(currentUserId);

        if (!isAdmin && !isOwner) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
    }

    private String storeFileToDisk(MultipartFile file) {
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

            return newFileName;

        } catch (IOException e) {
            throw new RuntimeException("Could not store file " + file.getOriginalFilename(), e);
        }
    }

    private void deleteFileFromDisk(String fileUrl) {
        try {
            if (fileUrl == null) return;

            String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
            Path filePath = Paths.get(UPLOAD_DIR).resolve(fileName);

            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            System.err.println("Could not delete file: " + fileUrl);
        }
    }
}