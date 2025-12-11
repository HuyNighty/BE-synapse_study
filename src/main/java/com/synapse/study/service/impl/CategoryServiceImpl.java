package com.synapse.study.service;

import com.synapse.study.dto.request.CategoryRequest;
import com.synapse.study.dto.response.CategoryResponse;
import com.synapse.study.entity.Category;
import com.synapse.study.enums.ErrorCode;
import com.synapse.study.exception.AppException;
import com.synapse.study.mapper.CategoryMapper;
import com.synapse.study.repository.CategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryServiceImpl implements CategoryService {

    static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;

    @Override
    public CategoryResponse create(CategoryRequest request) {
        String slug = toSlug(request.name());

        if (categoryRepository.existsBySlug(slug))
            throw new AppException(ErrorCode.SLUG_EXISTED);

        Category category = categoryMapper.toCategory(request);
        category.setSlug(slug);

        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Override
    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
    }

    @Override
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        categoryMapper.updateCategory(category, request);

        if (request.name() != null && !request.name().isEmpty()) {
            category.setSlug(toSlug(request.name()));
        }

        return categoryMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Override
    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public String toSlug(String input) {
        if (input == null) return "";

        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");

        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);

        String slug = NONLATIN.matcher(normalized).replaceAll("");

        return slug.toLowerCase(Locale.ENGLISH);
    }
}