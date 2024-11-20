package com.anamnesys.controller;

import com.anamnesys.controller.dto.RecordRequest;
import com.anamnesys.controller.dto.RecordResponse;
import com.anamnesys.repository.model.RecordModel;
import com.anamnesys.service.RecordService;
import com.anamnesys.util.RecordMapper;
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

@RestController
@RequestMapping("{userId}/record")
public class RecordController {

    @Autowired
    private RecordService recordService;
    private static final Logger logger = LoggerFactory.getLogger(RecordController.class);

    @PostMapping("/create")
    public ResponseEntity<RecordResponse> createRecord(@PathVariable Long userId, @Valid @RequestBody RecordRequest record) {

        logger.info("Received request to create record: {}", record);
        RecordModel model = RecordMapper.toModel(record, userId, null);
        recordService.createRecord(model);
        logger.info("Record created successfully with ID: {} ", model.getId());

        return new ResponseEntity<>(RecordMapper.toRecordResponse(model), HttpStatus.CREATED);
    }

    @PostMapping("{recordId}/update")
    public ResponseEntity<RecordResponse> updateRecord(@PathVariable Long userId, @PathVariable Long recordId, @Valid @RequestBody RecordRequest record) {

        logger.info("Received request to update record: {}", record);
        RecordModel model = RecordMapper.toModel(record, userId, recordId);
        RecordModel modelUpdated = recordService.updateRecord(model);
        logger.info("Record updated successfully with ID: {} ", model.getId());

        return new ResponseEntity<>(RecordMapper.toRecordResponse(modelUpdated), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<RecordResponse>> getRecordsByUserId(
            @PathVariable Long userId,
            @PageableDefault(size = 10) Pageable pageable) {

        logger.info("Received request get all records for userId: {}", userId);
        Page<RecordModel> records = recordService.getRecordsByUserId(userId, pageable);

        Page<RecordResponse> response = records.map(RecordMapper::toRecordResponse);
        logger.info("Process get all records for userId: {} ", userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{recordId}")
    public ResponseEntity<RecordResponse> getRecordById(
            @PathVariable Long userId,
            @PathVariable Long recordId) {

        logger.info("Received request get record by id: {} {}", userId, recordId);
        RecordModel record = recordService.getRecordById(recordId, userId);

        logger.info("Process get record by id: {} {}", userId, recordId);
        return ResponseEntity.ok(RecordMapper.toRecordResponse(record));
    }



}
