package com.anamnesys.repository;

import com.anamnesys.repository.model.PatientModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PatientRepository extends JpaRepository<PatientModel, Long> {
    Page<PatientModel> findByUserId(Long userId, Pageable pageable);
    Page<PatientModel> findByUserIdAndNameContainingIgnoreCase(Long userId, String name, Pageable pageable);
    Page<PatientModel> findByUserIdAndEmailIgnoreCase(Long userId, String email, Pageable pageable);
    @Query("SELECT p FROM PatientModel p WHERE MONTH(p.birth) = :month AND DAY(p.birth) = :day")
    List<PatientModel> findByBirth(@Param("month") int month, @Param("day") int day);
}
