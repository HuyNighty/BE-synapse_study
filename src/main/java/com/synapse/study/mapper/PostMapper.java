package com.synapse.study.mapper;

import com.synapse.study.dto.request.PostCreationRequest;
import com.synapse.study.dto.request.PostUpdateRequest;
import com.synapse.study.dto.response.PostResponse;
import com.synapse.study.entity.Post;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CategoryMapper.class})
public interface PostMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "status", constant = "PUBLISHED")
    @Mapping(target = "viewCount", constant = "0L")
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "thumbnail", ignore = true)
    @Mapping(target = "tags", ignore = true)
    Post toPost(PostCreationRequest request);

    PostResponse toPostResponse(Post post);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "viewCount", ignore = true)
    @Mapping(target = "publishedAt", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "thumbnail", ignore = true)
    @Mapping(target = "tags", ignore = true)
    void updatePost(@MappingTarget Post post, PostUpdateRequest request);
}