package com.hbq.biddingsystem.services.impl;


import com.hbq.biddingsystem.dtos.ProductDto;
import com.hbq.biddingsystem.entities.Product;
import com.hbq.biddingsystem.repository.ProductRepository;
import com.hbq.biddingsystem.services.ProductService;
import com.hbq.biddingsystem.utils.OurMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackOn = Throwable.class)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final OurMapper modelMapper;
    public ProductServiceImpl(ProductRepository productRepository, OurMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = productRepository.save(modelMapper.toEntity(productDto));
        return modelMapper.toDto(product);
    }


    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        Product product = productRepository.save(modelMapper.toEntity(productDto));
        return modelMapper.toDto(product);
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
            return modelMapper.toDto(optional.get());
        }
        throw new NullPointerException("Entity cannot be found.");
    }

    @Override
    public List<ProductDto> findAll() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(modelMapper::toDto).collect(Collectors.toList());
    }


    //** Custom code **//


}
