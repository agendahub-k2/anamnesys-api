package com.anamnesys.repository;

import com.anamnesys.repository.model.PatientModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<PatientModel, Long> {
}
