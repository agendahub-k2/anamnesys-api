package com.anamnesys.repository;

import com.anamnesys.repository.model.RecordModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface RecordRepository extends JpaRepository<RecordModel, Long> {
    boolean existsByNameAndUserId(String name, Long userId);

    Page<RecordModel> findByUserId(Long userId, Pageable pageable);
    List<RecordModel> findByUserIdAndNameContaining(Long userId, String name);
    void deleteByIdAndUserId(Long id, Long userId);
}
