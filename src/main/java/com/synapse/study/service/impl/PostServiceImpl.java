package com.synapse.study.service.impl;

import com.synapse.study.dto.request.PostCreationRequest;
import com.synapse.study.dto.response.PostResponse;
import com.synapse.study.entity.*;
import com.synapse.study.enums.ErrorCode;
import com.synapse.study.exception.AppException;
import com.synapse.study.mapper.PostMapper;
import com.synapse.study.repository.*;
import com.synapse.study.service.CategoryService;
import com.synapse.study.service.PostService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImpl implements PostService {

    PostRepository postRepository;
    CategoryRepository categoryRepository;
    UserRepository userRepository;
    AssetRepository assetRepository;
    PostMapper postMapper;
    CategoryService categoryService;

    @Override
    public PostResponse getPostBySlug(String slug) {
        Post post = postRepository.findBySlug(slug)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        post.setViewCount(post.getViewCount() + 1);
        postRepository.save(post);

        return postMapper.toPostResponse(post);
    }

    @Override
    @Transactional
    public PostResponse createPost(PostCreationRequest request) {
        var context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();
        User author = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Post post = postMapper.toPost(request);
        post.setUser(author);

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        post.setCategory(category);

        if (request.thumbnailId() != null) {
            Asset thumbnail = assetRepository.findById(request.thumbnailId())
                    .orElseThrow(() -> new RuntimeException("Thumbnail not found"));
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
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll().stream()
                .map(postMapper::toPostResponse)
                .toList();
    }
}