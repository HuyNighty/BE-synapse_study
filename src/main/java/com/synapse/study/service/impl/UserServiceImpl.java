package com.synapse.study.service.impl;

import com.synapse.study.dto.request.UserCreationRequest;
import com.synapse.study.dto.response.UserResponse;
import com.synapse.study.entity.Role;
import com.synapse.study.entity.User;
import com.synapse.study.entity.UserRole;
import com.synapse.study.enums.ErrorCode;
import com.synapse.study.exception.AppException;
import com.synapse.study.mapper.UserMapper;
import com.synapse.study.repository.RoleRepository;
import com.synapse.study.repository.UserRepository;
import com.synapse.study.repository.UserRoleRepository;
import com.synapse.study.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    UserRoleRepository userRoleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse createUser(UserCreationRequest request) {

        if (userRepository.existsByUsername(request.username()))
            throw new AppException(ErrorCode.USER_EXISTED);

        if (userRepository.existsByEmail(request.email()))
            throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);

        user.setPasswordHash(passwordEncoder.encode(request.password()));

        user.setUserRoles(new HashSet<>());

        User savedUser = userRepository.save(user);

        Role userRoleDef = roleRepository.findByName("USER")
                .orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION));

        UserRole userRole = UserRole.builder()
                .id(new UserRole.UserRoleKey(savedUser.getId(), userRoleDef.getId()))
                .user(savedUser)
                .role(userRoleDef)
                .build();

        userRoleRepository.save(userRole);

        return userMapper.toUserResponse(savedUser);
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String userId = context.getAuthentication().getName();

        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return userMapper.toUserResponse(user);
    }

    @Override
    public List<UserResponse> getUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse)
                .toList();
    }
}