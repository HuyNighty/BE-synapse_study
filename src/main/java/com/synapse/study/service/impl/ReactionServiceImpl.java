package com.synapse.study.service.impl;

import com.synapse.study.dto.request.ReactionRequest;
import com.synapse.study.dto.response.ReactionResponse;
import com.synapse.study.entity.Post;
import com.synapse.study.entity.Reaction;
import com.synapse.study.entity.User;
import com.synapse.study.enums.ErrorCode;
import com.synapse.study.exception.AppException;
import com.synapse.study.mapper.ReactionMapper;
import com.synapse.study.repository.PostRepository;
import com.synapse.study.repository.ReactionRepository;
import com.synapse.study.service.ReactionService;
import com.synapse.study.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReactionServiceImpl implements ReactionService {

    ReactionRepository reactionRepository;
    PostRepository postRepository;
    SecurityUtils securityUtils;
    ReactionMapper reactionMapper;

    @Override
    @Transactional
    public ReactionResponse react(ReactionRequest request) {
        User user = securityUtils.getCurrentUser();

        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        Optional<Reaction> existingReaction = reactionRepository.findByUserIdAndPostId(user.getId(), post.getId());

        long total = reactionRepository.countByPostId(post.getId());

        if (existingReaction.isPresent()) {
            Reaction reaction = existingReaction.get();
            if (reaction.getType() == request.type()) {
                reactionRepository.delete(reaction);
                return new ReactionResponse(null, post.getId(), null, null, total - 1);
            }
            else {
                reaction.setType(request.type());
                Reaction saved = reactionRepository.save(reaction);
                return new ReactionResponse(saved.getId(), post.getId(), saved.getType(), null, total);
            }
        } else {
            Reaction reaction = Reaction.builder()
                    .user(user)
                    .post(post)
                    .type(request.type())
                    .build();
            Reaction saved = reactionRepository.save(reaction);
            return new ReactionResponse(saved.getId(), post.getId(), saved.getType(), null, total + 1);
        }
    }

    @Override
    public Page<ReactionResponse> getReactionsByPost(UUID postId, Pageable pageable) {
        if (!postRepository.existsById(postId)) {
            throw new AppException(ErrorCode.POST_NOT_FOUND);
        }

        return reactionRepository.findByPostId(postId, pageable)
                .map(reactionMapper::toReactionResponse);
    }
}