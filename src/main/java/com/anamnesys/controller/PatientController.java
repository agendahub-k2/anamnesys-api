package com.anamnesys.controller;

import com.anamnesys.controller.dto.PatientRequest;
import com.anamnesys.controller.dto.PatientResponse;
import com.anamnesys.repository.model.PatientModel;
import com.anamnesys.service.PatientService;
import com.anamnesys.util.PatientMapper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("{userId}/patientId")
public class PatientController {

    @Autowired
    private PatientService patientService;
    private static final Logger logger = LoggerFactory.getLogger(PatientController.class);

    @PostMapping("/create")
    public ResponseEntity<PatientResponse> createPatient(@PathVariable Long userId, @Valid @RequestBody PatientRequest patientRequest) {

        logger.info("Received request to create Patient: {}", patientRequest);

        PatientModel patientModel = PatientMapper.toModel(patientRequest, null, userId);
        patientService.createPatient(patientModel);

        logger.info("Patient created successfully with ID: {}", patientModel);

        return new ResponseEntity<>(PatientMapper.toUserResponse(patientModel), HttpStatus.CREATED);
    }

    @PostMapping("/update/{patientId}")
    public ResponseEntity<PatientResponse> updatePatient(@PathVariable Long userId, @PathVariable Long patientId, @Valid @RequestBody PatientRequest patientRequest) {

        logger.info("Received request to update Patient: {}", patientRequest);

        PatientModel patientModel = PatientMapper.toModel(patientRequest, patientId, userId);
        patientService.updatePatient(patientModel);

        logger.info("Patient updated successfully with ID: {}", patientModel);

        return new ResponseEntity<>(PatientMapper.toUserResponse(patientModel), HttpStatus.CREATED);
    }
}
