package com.anamnesys.repository;

import com.anamnesys.repository.model.TermModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TermRepository extends JpaRepository<TermModel, Long> {
    List<TermModel> findByUserIdIsNull();
    List<TermModel> findByUserId(Long userId);
}
