package com.anamnesys.service;

import com.anamnesys.exception.TemplateNotFoundException;
import com.anamnesys.repository.TemplateRepository;
import com.anamnesys.repository.model.TemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TemplateService {

    @Autowired
    TemplateRepository repository;

    public List<TemplateModel> getTemplates(String category) {

        List<TemplateModel> templateModels;

        if (!category.isEmpty()) {
            templateModels = repository.findAllBySegment_Category(category);
        } else {
            templateModels = repository.findAll();
        }
        return templateModels;
    }

    public TemplateModel getTemplateById(Long templateId) {
        return repository.findById(templateId).orElseThrow(TemplateNotFoundException::new);
    }
}
