package com.Cataloger.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.Cataloger.dto.request.ProductRequest;
import com.Cataloger.dto.response.ProductResponse;
import com.Cataloger.entity.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponse toResponse(Product product);

//    @Mapping(target = "category", source = "categoryId")
    Product toEntity(ProductRequest request);

//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "category", source = "categoryId")
    void updateEntity(ProductRequest request, @MappingTarget Product product);
}
