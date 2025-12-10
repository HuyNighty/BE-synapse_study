package com.synapse.study.service;

import com.synapse.study.dto.request.CategoryRequest;
import com.synapse.study.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse create(CategoryRequest request);
    String toSlug(String input);
    List<CategoryResponse> getAll();
}
