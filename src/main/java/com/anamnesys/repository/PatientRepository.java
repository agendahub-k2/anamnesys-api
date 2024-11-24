package com.anamnesys.repository;

import com.anamnesys.repository.model.PatientModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<PatientModel, Long> {
    Page<PatientModel> findByUserId(Long userId, Pageable pageable);

    Page<PatientModel> findByUserIdAndNameContainingIgnoreCase(Long userId, String name, Pageable pageable);

}
