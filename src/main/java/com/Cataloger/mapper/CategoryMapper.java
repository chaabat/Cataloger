package com.Cataloger.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import com.Cataloger.dto.request.CategoryRequest;
import com.Cataloger.dto.response.CategoryResponse;
import com.Cataloger.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toResponse(Category category);
    Category toEntity(CategoryRequest request);
    void updateEntity(CategoryRequest request, @MappingTarget Category category);
}
