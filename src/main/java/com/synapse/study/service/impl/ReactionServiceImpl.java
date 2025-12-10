package com.synapse.study.service;

import com.synapse.study.dto.request.ReactionRequest;
import com.synapse.study.dto.response.ReactionResponse;
import com.synapse.study.entity.Post;
import com.synapse.study.entity.Reaction;
import com.synapse.study.entity.User;
import com.synapse.study.enums.ErrorCode;
import com.synapse.study.exception.AppException;
import com.synapse.study.repository.PostRepository;
import com.synapse.study.repository.ReactionRepository;
import com.synapse.study.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
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
    UserRepository userRepository;

    @Override
    @Transactional
    public ReactionResponse react(ReactionRequest request) {
        var context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        Optional<Reaction> existingReaction = reactionRepository.findByUserIdAndPostId(user.getId(), post.getId());

        if (existingReaction.isPresent()) {
            Reaction reaction = existingReaction.get();
            if (reaction.getType() == request.type()) {
                reactionRepository.delete(reaction);
                return new ReactionResponse(post.getId(), null, reactionRepository.countByPostId(post.getId()));
            }
            else {
                reaction.setType(request.type());
                reactionRepository.save(reaction);
                return new ReactionResponse(post.getId(), request.type(), reactionRepository.countByPostId(post.getId()));
            }
        } else {
            Reaction reaction = Reaction.builder()
                    .user(user)
                    .post(post)
                    .type(request.type())
                    .build();
            reactionRepository.save(reaction);
            return new ReactionResponse(post.getId(), request.type(), reactionRepository.countByPostId(post.getId()) + 1); // +1 vì cái vừa save chưa kịp commit để count
        }
    }
}