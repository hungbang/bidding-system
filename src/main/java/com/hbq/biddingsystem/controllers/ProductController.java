package com.hbq.biddingsystem.controllers;

import com.hbq.biddingsystem.dtos.ProductDto;
import com.hbq.biddingsystem.services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto){
        final ProductDto productDtoSaved = productService.createProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productDtoSaved);
    }

    @PutMapping
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto){
        final ProductDto productDtoSaved = productService.updateProduct(productDto);
        return ResponseEntity.status(HttpStatus.OK).body(productDtoSaved);
    }

    @DeleteMapping
    public ResponseEntity<ProductDto> deleteProduct(@RequestParam String productId){
        productService.deleteProduct(productId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable("id") String productId){
        final ProductDto productDto = productService.findProductById(productId);
        return ResponseEntity.status(HttpStatus.OK).body(productDto);
    }


    @GetMapping
    public ResponseEntity<List<ProductDto>> findAll(){
        final List<ProductDto> productDto = productService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(productDto);
    }
}
