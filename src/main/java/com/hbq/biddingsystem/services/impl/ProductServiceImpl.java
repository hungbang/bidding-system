package com.hbq.biddingsystem.services.impl;


import com.hbq.biddingsystem.dtos.ProductDto;
import com.hbq.biddingsystem.entities.Product;
import com.hbq.biddingsystem.repository.ProductRepository;
import com.hbq.biddingsystem.services.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = productRepository.save(productDto.toEntity());
        return product.toDto();
    }


    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        Product product = productRepository.save(productDto.toEntity());
        return product.toDto();
    }

    @Override
    public void deleteProduct(String productId) {
        Optional<Product> optional = productRepository.findById(productId);
        optional.ifPresent(productRepository::delete);
    }

    @Override
    public ProductDto findProductById(String productId) {
        Optional<Product> optional =  productRepository.findById(productId);
        if (optional.isPresent()){
            return optional.get().toDto();
        }
        throw new NullPointerException("Entity cannot be found.");
    }

    @Override
    public List<ProductDto> findAll() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(Product::toDto).collect(Collectors.toList());
    }
}
