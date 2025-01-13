package com.anamnesys.repository;

import com.anamnesys.repository.model.RecordSendModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface RecordSendRepository extends JpaRepository<RecordSendModel, String> {
    List<RecordSendModel> findByUserIdAndClientId(Long userId, Long clientId);
}
