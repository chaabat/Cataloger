package com.Cataloger.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import com.Cataloger.dto.request.UserRequest;
import com.Cataloger.dto.response.UserResponse;
import com.Cataloger.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(User user);
    
    @Mapping(target = "roles", ignore = true)
    User toEntity(UserRequest request);
    
    @Mapping(target = "roles", ignore = true)
    void updateEntity(UserRequest request, @MappingTarget User user);
}
