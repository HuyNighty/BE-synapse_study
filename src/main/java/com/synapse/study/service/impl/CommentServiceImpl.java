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
import com.synapse.study.service.CommentService;
import com.synapse.study.utils.SecurityUtils; // <--- Import Utils
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
    CommentMapper commentMapper;
    SecurityUtils securityUtils;

    @Override
    @Transactional
    public CommentResponse createComment(CommentRequest request) {
        User user = securityUtils.getCurrentUser();

        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        Comment comment = commentMapper.toComment(request);
        comment.setUser(user);
        comment.setPost(post);

        if (request.parentId() != null) {
            Comment parentComment = commentRepository.findById(request.parentId())
                    .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

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

    @Override
    public List<CommentResponse> getReplies(UUID commentId, int page, int size) {
        if (!commentRepository.existsById(commentId)) {
            throw new AppException(ErrorCode.COMMENT_NOT_FOUND);
        }

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("createdAt").ascending());
        Page<Comment> pageResult = commentRepository.findByParentIdOrderByCreatedAtAsc(commentId, pageable);

        return pageResult.stream()
                .map(commentMapper::toCommentResponse)
                .toList();
    }

    @Override
    @Transactional
    public CommentResponse updateComment(UUID commentId, CommentRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

        User currentUser = securityUtils.getCurrentUser();

        if (!comment.getUser().getId().equals(currentUser.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        commentMapper.updateComment(comment, request);
        return commentMapper.toCommentResponse(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public void deleteComment(UUID commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

        var context = SecurityContextHolder.getContext();
        String currentUserId = context.getAuthentication().getName();
        var authorities = context.getAuthentication().getAuthorities();

        boolean isOwner = comment.getUser().getId().toString().equals(currentUserId);
        boolean isAdmin = authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isOwner && !isAdmin) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        commentRepository.delete(comment);
    }
}