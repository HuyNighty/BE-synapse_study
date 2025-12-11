package com.synapse.study.service;

import com.synapse.study.dto.request.PostCreationRequest;
import com.synapse.study.dto.request.PostUpdateRequest;
import com.synapse.study.dto.response.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;


public interface PostService {

    PostResponse createPost(PostCreationRequest request);
    Page<PostResponse> getAllPosts(Pageable pageable);
    PostResponse getPostBySlug(String slug);
    PostResponse updatePost(UUID postId, PostUpdateRequest request);
    void deletePost(UUID postId);
    Page<PostResponse> getMyPosts(Pageable pageable);
}
