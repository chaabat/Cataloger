package com.Cataloger.mapper;

import com.Cataloger.dto.request.CategoryRequest;
import com.Cataloger.dto.response.CategoryResponse;
import com.Cataloger.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toResponse(Category category);
    Category toEntity(CategoryRequest request);
    void updateEntity(CategoryRequest request, @MappingTarget Category category);
}