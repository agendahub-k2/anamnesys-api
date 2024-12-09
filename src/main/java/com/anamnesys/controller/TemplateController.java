package com.anamnesys.controller;

import com.anamnesys.controller.dto.TemplateResponse;
import com.anamnesys.controller.dto.TemplatesBySegmentResponse;
import com.anamnesys.repository.model.TemplateModel;
import com.anamnesys.service.TemplateService;
import com.anamnesys.util.TemplateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("template")
public class TemplateController {

    @Autowired
    private TemplateService templateService;
    private static final Logger logger = LoggerFactory.getLogger(TemplateController.class);


    @GetMapping
    public ResponseEntity<TemplatesBySegmentResponse> getAllTemplates() {

        logger.info("Received request get all templates");
        List<TemplateModel> templates = templateService.getTemplates();
        TemplatesBySegmentResponse templatesBySegmentResponse = TemplateMapper.toResponse(templates);

        logger.info("Process get all templates");
        return ResponseEntity.ok(templatesBySegmentResponse);
    }

    @GetMapping("/{templateId}")
    public ResponseEntity<TemplateResponse> getTemplateById(@PathVariable Long templateId) {

        logger.info("Received request get template by id");
        TemplateModel template = templateService.getTemplateById(templateId);
        TemplateResponse templateModel = TemplateMapper.toResponse(template);

        logger.info("Process get template by id");
        return ResponseEntity.ok(templateModel);
    }
}
