package com.hbq.biddingsystem.repository;

import com.hbq.biddingsystem.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
}
