package com.synapse.study.mapper;

import com.synapse.study.dto.response.ReactionResponse;
import com.synapse.study.entity.Reaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ReactionMapper {

    @Mapping(target = "postId", source = "post.id")
    @Mapping(target = "totalReactions", ignore = true)
    ReactionResponse toReactionResponse(Reaction reaction);
}
