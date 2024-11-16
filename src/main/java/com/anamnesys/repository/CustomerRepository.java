package com.anamnesys.repository;

import com.anamnesys.repository.model.CustomerModel;
import com.anamnesys.repository.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<CustomerModel, Long> {
}
