package com.anamnesys.repository;

import com.anamnesys.repository.model.SegmentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface SegmentRepository extends JpaRepository<SegmentModel, Long> {

    @Transactional
    SegmentModel findByNameAndCategory(String name, String category);
}

