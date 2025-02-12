package com.anamnesys.repository;

import com.anamnesys.repository.model.RecordModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface RecordRepository extends JpaRepository<RecordModel, Long> {

    boolean existsByNameAndUserId(String name, Long userId);

    Page<RecordModel> findByUserId(Long userId, Pageable pageable);

    List<RecordModel> findByUserIdAndNameContaining(Long userId, String name);

    void deleteByIdAndUserId(Long id, Long userId);

    @Query("SELECT r.name FROM RecordModel r WHERE r.id = :id")
    Optional<String> findNameById(@Param("id") Long id);

    Optional<RecordModel> findByIdAndUserId(Long recordId, Long userId);
}
