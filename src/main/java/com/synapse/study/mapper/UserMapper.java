package com.synapse.study.mapper;

import com.synapse.study.dto.request.UserCreationRequest;
import com.synapse.study.dto.response.UserResponse;
import com.synapse.study.entity.User;
import com.synapse.study.entity.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userRoles", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    User toUser(UserCreationRequest request);

    @Mapping(target = "roles", source = "userRoles", qualifiedByName = "mapRoles")
    UserResponse toUserResponse(User user);

    @Named("mapRoles")
    default Set<String> mapRoles(Set<UserRole> userRoles) {
        if (userRoles == null) return Set.of();
        return userRoles.stream()
                .map(userRole -> userRole.getRole().getName())
                .collect(Collectors.toSet());
    }
}