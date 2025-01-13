package com.anamnesys.controller;

import com.anamnesys.controller.dto.PatientRequest;
import com.anamnesys.controller.dto.PatientResponse;
import com.anamnesys.repository.model.PatientModel;
import com.anamnesys.repository.model.RecordSendModel;
import com.anamnesys.service.PatientService;
import com.anamnesys.util.PatientMapper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/update/{patientId}")
    public ResponseEntity<PatientResponse> updatePatient(@PathVariable Long userId, @PathVariable Long patientId, @Valid @RequestBody PatientRequest patientRequest) {

        logger.info("Received request to update Patient: {} userId {} patientId {}", patientRequest, userId, patientId);

        PatientModel patientModel = PatientMapper.toModel(patientRequest, patientId, userId);
        patientService.updatePatient(patientModel);

        logger.info("Patient updated successfully with ID: {}", patientModel);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/find")
    public ResponseEntity<Page<PatientResponse>> getPatientsByName(
            @PathVariable Long userId,
            @RequestParam String name,
            @PageableDefault Pageable pageable) {

        logger.info("Received request get all patients for userId and name: {} {}", userId, name);
        Page<PatientModel> records = patientService.getAllPatientsByUserIdAndName(userId, name, pageable);

        Page<PatientResponse> response = records.map(PatientMapper::toUserResponse);
        logger.info("Retrieved {} patients for userId: {} and name: {}", response.getTotalElements(), userId, name);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<PatientResponse>> getPatientsByUserId(
            @PathVariable Long userId,
            @PageableDefault Pageable pageable) {

        logger.info("Received request get all patients for userId: {}", userId);
        Page<PatientModel> records = patientService.getAllPatientsByUserId(userId, pageable);

        Page<PatientResponse> response = records.map(PatientMapper::toUserResponse);
        logger.info("Process get all patients for userId: {} ", userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<PatientResponse> getPatientById(
            @PathVariable Long patientId) {

        logger.info("Received request get patient for patientId: {}", patientId);
        PatientModel patientModel = patientService.getPatientById(patientId);

        logger.info("Process get patient for patientId: {}", patientId);
        return ResponseEntity.ok(PatientMapper.toUserResponse(patientModel));
    }

    @GetMapping("records/{patientId}")
    public ResponseEntity<List<RecordSendModel>> getRecordsByUserIdPatientId(
            @PathVariable Long userId,
            @PathVariable Long patientId) {

        logger.info("Received request get records for patientId: {} and userId {}", patientId, userId);
        List<RecordSendModel> sendRecords = patientService.getRecordsByUserIdPatientId(patientId, userId);

        logger.info("Process get records for patientId: {} and userId {}", patientId, userId);
        return ResponseEntity.ok(sendRecords);
    }

}
