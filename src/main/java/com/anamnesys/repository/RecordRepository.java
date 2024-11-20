package com.anamnesys.repository;

import com.anamnesys.repository.model.RecordModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RecordRepository extends JpaRepository<RecordModel, Long> {
    boolean existsByName(String name);

    Page<RecordModel> findByUserId(Long userId, Pageable pageable);
}
