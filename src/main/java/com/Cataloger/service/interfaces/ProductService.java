package com.Cataloger.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.Cataloger.dto.request.ProductRequest;
import com.Cataloger.dto.response.ProductResponse;

public interface ProductService {
    Page<ProductResponse> getAllProducts(Pageable pageable);
    Page<ProductResponse> searchProducts(String designation, Pageable pageable);
    Page<ProductResponse> getProductsByCategory(Long categoryId, Pageable pageable);
    ProductResponse getProduct(Long id);
    ProductResponse createProduct(ProductRequest request);
    ProductResponse updateProduct(Long id, ProductRequest request);
    void deleteProduct(Long id);
}
