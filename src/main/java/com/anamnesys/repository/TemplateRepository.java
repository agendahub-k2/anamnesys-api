package com.anamnesys.repository;

import com.anamnesys.repository.model.TemplateModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TemplateRepository extends JpaRepository<TemplateModel, Long> {

    List<TemplateModel> findAllBySegment_Category(String category);
}
