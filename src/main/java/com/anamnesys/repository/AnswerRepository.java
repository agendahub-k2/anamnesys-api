package com.anamnesys.repository;

import com.anamnesys.repository.model.AnswerModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<AnswerModel, String> {
}
