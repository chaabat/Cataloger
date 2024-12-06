package com.Cataloger.mapper;

import org.mapstruct.Mapper;
<<<<<<< HEAD
import org.mapstruct.Mapping;
=======
>>>>>>> parent of 9d2fc01 (category , product and user done)
import org.mapstruct.MappingTarget;
import com.Cataloger.dto.request.CategoryRequest;
import com.Cataloger.dto.response.CategoryResponse;
import com.Cataloger.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponse toResponse(Category category);

    Category toEntity(CategoryRequest request);

    @Mapping(target = "id", ignore = true)  
    void updateEntity(CategoryRequest request, @MappingTarget Category category);
}
