package com.anamnesys.service;

import com.anamnesys.exception.TemplateNotFoundException;
import com.anamnesys.repository.TemplateRepository;
import com.anamnesys.repository.model.TemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TemplateService {

    @Autowired
    TemplateRepository repository;

    public List<TemplateModel> getTemplates() {
        return repository.findAll();
    }

    public TemplateModel getTemplateById(Long templateId) {
        return repository.findById(templateId).orElseThrow(TemplateNotFoundException::new);
    }
}
