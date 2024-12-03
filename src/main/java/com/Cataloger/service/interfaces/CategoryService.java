package com.Cataloger.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.Cataloger.dto.request.CategoryRequest;
import com.Cataloger.dto.response.CategoryResponse;

public interface CategoryService {
    Page<CategoryResponse> getAllCategories(Pageable pageable);
    Page<CategoryResponse> searchCategories(String name, Pageable pageable);
    CategoryResponse getCategory(Long id);
    CategoryResponse createCategory(CategoryRequest request);
    CategoryResponse updateCategory(Long id, CategoryRequest request);
    void deleteCategory(Long id);
}
