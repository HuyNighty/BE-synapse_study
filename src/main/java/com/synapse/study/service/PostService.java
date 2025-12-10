package com.synapse.study.service;

import com.synapse.study.dto.request.PostCreationRequest;
import com.synapse.study.dto.response.PostResponse;

import java.util.List;

public interface PostService {

    PostResponse createPost(PostCreationRequest request);
    List<PostResponse> getAllPosts();
    PostResponse getPostBySlug(String slug);
}
