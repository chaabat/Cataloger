package com.Cataloger.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductRequest {

    @NotBlank(message = "Designation is mandatory")
    @Size(max = 100, message = "Designation must not exceed 100 characters")
    private String designation;

    @NotNull(message = "Price is mandatory")
    @Positive(message = "Price must be positive")
    private Double prix;

    @NotNull(message = "Quantity is mandatory")
    @Positive(message = "Quantity must be positive")
    private int quantite;

    @NotNull(message = "Category ID is mandatory")
    private Long categoryId; 
}
