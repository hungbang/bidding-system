package com.hbq.biddingsystem.services.impl;

import com.google.common.collect.Lists;
import com.hbq.biddingsystem.entities.Product;
import com.hbq.biddingsystem.repository.ProductRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PrepareDataForTest {

    private final ProductRepository productRepository;

    public PrepareDataForTest(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void run(){
        this.createProducts();
    }

    private void createProducts() {
        Product product = Product.builder().title("Iphone 6")
                .description("a product from Apple")
                .name("Iphone 6")
                .quantity(1)
                .price(BigDecimal.valueOf(1000))
                .build();
        Product product1 = Product.builder().title("Iphone 7")
                .description("a product from Apple")
                .name("Iphone 7")
                .quantity(1)
                .price(BigDecimal.valueOf(1000))
                .build();
        Product product2 = Product.builder().title("Iphone 8")
                .description("a product from Apple")
                .name("Iphone 8")
                .quantity(1)
                .price(BigDecimal.valueOf(1000))
                .build();
        Product product3 = Product.builder().title("Iphone 9")
                .description("a product from Apple")
                .name("Iphone 9")
                .quantity(1)
                .price(BigDecimal.valueOf(1000))
                .build();
        productRepository.saveAll(Lists.newArrayList(product, product1, product2, product3));

    }
}
