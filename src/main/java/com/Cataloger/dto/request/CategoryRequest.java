package com.Cataloger.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data  
public class CategoryRequest {
    @NotNull(message = "Name cannot be null")  
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")  
    private String name;  

    @Size(max = 255, message = "Description must be up to 255 characters")  
    private String description;  
}
