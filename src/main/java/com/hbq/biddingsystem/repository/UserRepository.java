package com.hbq.biddingsystem.repository;

import com.hbq.biddingsystem.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
