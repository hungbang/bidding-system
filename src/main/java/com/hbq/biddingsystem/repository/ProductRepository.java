package com.hbq.biddingsystem.repository;

import com.hbq.biddingsystem.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
}
