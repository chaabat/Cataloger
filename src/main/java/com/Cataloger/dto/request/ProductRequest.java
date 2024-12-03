package com.Cataloger.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductRequest {
    @NotNull(message = "Designation cannot be null")
    @Size(min = 1, max = 100, message = "Designation must be between 1 and 100 characters")
    private String designation;

    @NotNull(message = "Prix cannot be null")
    @Positive(message = "Prix must be positive")
    private Double prix;

    @NotNull(message = "Quantite cannot be null")
    @PositiveOrZero(message = "Quantite must be zero or positive")
    private Integer quantite;

    @NotNull(message = "Category ID cannot be null")
    private Long categoryId;
}
