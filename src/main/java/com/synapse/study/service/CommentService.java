package com.synapse.study.service;

import com.synapse.study.dto.request.CommentRequest;
import com.synapse.study.dto.response.CommentResponse;

import java.util.List;
import java.util.UUID;

public interface CommentService {

    CommentResponse createComment(CommentRequest request);
    List<CommentResponse> getCommentsByPost(UUID postId, int page, int size);
    void deleteComment(UUID commentId);
    CommentResponse updateComment(UUID commentId, CommentRequest request);
    List<CommentResponse> getReplies(UUID commentId, int page, int size);
}
