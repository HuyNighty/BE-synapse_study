package com.synapse.study.service.impl;

import com.synapse.study.dto.request.CommentRequest;
import com.synapse.study.dto.response.CommentResponse;
import com.synapse.study.entity.Comment;
import com.synapse.study.entity.Post;
import com.synapse.study.entity.User;
import com.synapse.study.enums.ErrorCode;
import com.synapse.study.exception.AppException;
import com.synapse.study.mapper.CommentMapper;
import com.synapse.study.repository.CommentRepository;
import com.synapse.study.repository.PostRepository;
import com.synapse.study.repository.UserRepository;
import com.synapse.study.service.CommentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentServiceImpl implements CommentService {

    CommentRepository commentRepository;
    PostRepository postRepository;
    UserRepository userRepository;
    CommentMapper commentMapper;

    @Override
    @Transactional
    public CommentResponse createComment(CommentRequest request) {
        var context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new RuntimeException("Post not found")); // Nên tạo ErrorCode

        Comment comment = commentMapper.toComment(request);
        comment.setUser(user);
        comment.setPost(post);

        if (request.parentId() != null) {
            Comment parentComment = commentRepository.findById(request.parentId())
                    .orElseThrow(() -> new RuntimeException("Parent comment not found"));

            if (!parentComment.getPost().getId().equals(post.getId())) {
                throw new RuntimeException("Parent comment does not belong to this post");
            }

            comment.setParent(parentComment);
        }

        return commentMapper.toCommentResponse(commentRepository.save(comment));
    }

    @Override
    public List<CommentResponse> getCommentsByPost(UUID postId, int page, int size) {
        if (!postRepository.existsById(postId)) {
            throw new AppException(ErrorCode.POST_NOT_FOUND);
        }

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").descending());

        Page<Comment> pageResult = commentRepository.findByPostIdAndParentIsNullOrderByCreatedAtDesc(postId, pageable);

        return pageResult.stream()
                .map(commentMapper::toCommentResponse)
                .toList();
    }
}