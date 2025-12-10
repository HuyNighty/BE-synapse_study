package com.synapse.study.mapper;

import com.synapse.study.dto.request.CommentRequest;
import com.synapse.study.dto.response.CommentResponse;
import com.synapse.study.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "post", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "replies", ignore = true)
    @Mapping(target = "isHidden", constant = "false")
    Comment toComment(CommentRequest request);

    CommentResponse toCommentResponse(Comment comment);
}