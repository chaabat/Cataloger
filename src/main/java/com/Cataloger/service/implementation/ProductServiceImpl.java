package com.Cataloger.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Cataloger.dto.request.ProductRequest;
import com.Cataloger.dto.response.ProductResponse;
import com.Cataloger.entity.Product;
import com.Cataloger.entity.Category;
import com.Cataloger.mapper.ProductMapper;
import com.Cataloger.repository.ProductRepository;
import com.Cataloger.repository.CategoryRepository;
import com.Cataloger.service.interfaces.ProductService;

@Service
@RequiredArgsConstructor
@Transactional
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
        return productRepository.findByDesignation(designation, pageable)
                .map(productMapper::toResponse);
    }

    @Override
    public Page<ProductResponse> getProductsByCategory(Long categoryId, Pageable pageable) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + categoryId));
        
        return productRepository.findByCategory(category, pageable)
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
        // Verify category exists
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + request.getCategoryId()));

        if (productRepository.existsByDesignation(request.getDesignation())) {
            throw new IllegalArgumentException("Product with this designation already exists");
        }

        Product product = productMapper.toEntity(request);
        product.setCategory(category);
        product = productRepository.save(product);
        return productMapper.toResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

        if (!product.getDesignation().equals(request.getDesignation()) && 
            productRepository.existsByDesignation(request.getDesignation())) {
            throw new IllegalArgumentException("Product with this designation already exists");
        }

         
        if (request.getCategoryId() != null && 
            !request.getCategoryId().equals(product.getCategory().getId())) {
            Category newCategory = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + request.getCategoryId()));
            product.setCategory(newCategory);
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
