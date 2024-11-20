package com.anamnesys.repository;

import com.anamnesys.repository.model.QuestionModel;
import com.anamnesys.repository.model.SegmentModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<QuestionModel, Long> {}

