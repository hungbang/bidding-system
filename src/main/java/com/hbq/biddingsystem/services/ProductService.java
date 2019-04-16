package com.hbq.biddingsystem.services;

import com.hbq.biddingsystem.dtos.ProductDto;

import java.util.List;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto);
    ProductDto updateProduct(ProductDto productDto);
    void deleteProduct(String productId);
    ProductDto findProductById(String productId);
    List<ProductDto> findAll();
}
