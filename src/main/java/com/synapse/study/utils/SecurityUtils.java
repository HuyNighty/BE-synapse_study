package com.synapse.study.utils;

import com.synapse.study.entity.User;
import com.synapse.study.enums.ErrorCode;
import com.synapse.study.exception.AppException;
import com.synapse.study.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

    private final UserRepository userRepository;

    public User getCurrentUser() {
        var context = SecurityContextHolder.getContext();

        if (context.getAuthentication() == null || !context.getAuthentication().isAuthenticated() ||
                "anonymousUser".equals(context.getAuthentication().getPrincipal())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        String userId = context.getAuthentication().getName();
        return userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }
}