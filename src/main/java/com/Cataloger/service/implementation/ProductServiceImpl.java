package com.Cataloger.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.Cataloger.dto.request.ProductRequest;
import com.Cataloger.dto.response.ProductResponse;
import com.Cataloger.entity.Product;
import com.Cataloger.mapper.ProductMapper;
import com.Cataloger.repository.ProductRepository;
import com.Cataloger.repository.CategoryRepository;
import com.Cataloger.service.interfaces.ProductService;
import jakarta.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    @Override
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::toResponse);
    }

    @Override
    public Page<ProductResponse> searchProducts(String designation, Pageable pageable) {
        return productRepository.findByDesignationContainingIgnoreCase(designation, pageable)
                .map(productMapper::toResponse);
    }

    @Override
    public Page<ProductResponse> getProductsByCategory(Long categoryId, Pageable pageable) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new EntityNotFoundException("Category not found with id: " + categoryId);
        }
        return productRepository.findByCategoryId(categoryId, pageable)
                .map(productMapper::toResponse);
    }

    @Override
    public ProductResponse getProduct(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        if (!categoryRepository.existsById(request.getCategoryId())) {
            throw new EntityNotFoundException("Category not found with id: " + request.getCategoryId());
        }
        Product product = productMapper.toEntity(request);
        product = productRepository.save(product);
        return productMapper.toResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
        
        if (!categoryRepository.existsById(request.getCategoryId())) {
            throw new EntityNotFoundException("Category not found with id: " + request.getCategoryId());
        }
        
        productMapper.updateEntity(request, product);
        product = productRepository.save(product);
        return productMapper.toResponse(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
}
