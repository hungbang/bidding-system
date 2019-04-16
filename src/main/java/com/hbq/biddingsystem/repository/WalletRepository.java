package com.hbq.biddingsystem.repository;

import com.hbq.biddingsystem.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, String> {
}
