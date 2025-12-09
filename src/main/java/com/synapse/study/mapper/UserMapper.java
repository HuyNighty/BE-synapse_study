package com.synapse.study.mapper;

import com.synapse.study.dto.request.UserCreationRequest;
import com.synapse.study.dto.response.UserResponse;
import com.synapse.study.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userRoles", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);
}