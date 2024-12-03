package com.Cataloger.dto.response;

import lombok.Data;

@Data
public class ProductResponse {
    private Long id;
    private String designation;
    private Double prix;
    private int quantite;
    private CategoryResponse category;
}
