package com.synapse.study.service.impl;

import com.synapse.study.dto.request.PostCreationRequest;
import com.synapse.study.dto.request.PostUpdateRequest;
import com.synapse.study.dto.response.PostResponse;
import com.synapse.study.entity.*;
import com.synapse.study.enums.ErrorCode;
import com.synapse.study.exception.AppException;
import com.synapse.study.mapper.PostMapper;
import com.synapse.study.repository.*;
import com.synapse.study.service.CategoryService;
import com.synapse.study.service.PostService;
import com.synapse.study.utils.SecurityUtils; // <--- Import Utils
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImpl implements PostService {

    PostRepository postRepository;
    CategoryRepository categoryRepository;
    AssetRepository assetRepository;
    PostMapper postMapper;
    CategoryService categoryService;
    SecurityUtils securityUtils;

    @Override
    @Transactional
    public PostResponse createPost(PostCreationRequest request) {
        User author = securityUtils.getCurrentUser();

        Post post = postMapper.toPost(request);
        post.setUser(author);

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        post.setCategory(category);

        if (request.thumbnailId() != null) {
            Asset thumbnail = assetRepository.findById(request.thumbnailId())
                    .orElseThrow(() -> new AppException(ErrorCode.THUMBNAIL_NOT_FOUND));
            post.setThumbnail(thumbnail);
        }

        String slug = categoryService.toSlug(post.getTitle());
        if (postRepository.existsBySlug(slug)) {
            slug += "-" + System.currentTimeMillis();
        }
        post.setSlug(slug);

        return postMapper.toPostResponse(postRepository.save(post));
    }

    @Override
    public Page<PostResponse> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(postMapper::toPostResponse);
    }

    // Nên dùng Redis để giải quyết vấn đề về view
    // Tạm thời: Tăng view mỗi khi F5 (refresh)
    // Cần tối ưu
    @Override
    @Transactional
    public PostResponse getPostBySlug(String slug) {
        Post post = postRepository.findBySlug(slug)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        post.setViewCount(post.getViewCount() + 1);

        return postMapper.toPostResponse(postRepository.save(post));
    }

    @Override
    @Transactional
    public PostResponse updatePost(UUID postId, PostUpdateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        User currentUser = securityUtils.getCurrentUser();
        if (!post.getUser().getId().equals(currentUser.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        postMapper.updatePost(post, request);

        if (request.categoryId() != null) {
            Category category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
            post.setCategory(category);
        }

        if (request.thumbnailId() != null) {
            Asset thumbnail = assetRepository.findById(request.thumbnailId())
                    .orElseThrow(() -> new AppException(ErrorCode.THUMBNAIL_NOT_FOUND));
            post.setThumbnail(thumbnail);
        }

        return postMapper.toPostResponse(postRepository.save(post));
    }

    @Override
    @Transactional
    public void deletePost(UUID postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        var context = SecurityContextHolder.getContext();
        String currentUserId = context.getAuthentication().getName();
        boolean isAdmin = context.getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        boolean isOwner = post.getUser().getId().toString().equals(currentUserId);

        if (!isOwner && !isAdmin) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        postRepository.delete(post);
    }

    @Override
    public Page<PostResponse> getMyPosts(Pageable pageable) {
        User currentUser = securityUtils.getCurrentUser();

        return postRepository.findByUserIdOrderByCreatedAtDesc(currentUser.getId(), pageable)
                .map(postMapper::toPostResponse);
    }
}