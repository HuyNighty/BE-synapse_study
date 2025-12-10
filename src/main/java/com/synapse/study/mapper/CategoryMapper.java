package com.synapse.study.mapper;

import com.synapse.study.dto.request.CategoryRequest;
import com.synapse.study.dto.response.CategoryResponse;
import com.synapse.study.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "slug", ignore = true)
    @Mapping(target = "posts", ignore = true)
    Category toCategory(CategoryRequest request);

    CategoryResponse toCategoryResponse(Category category);


}