package com.anamnesys.controller;

import com.anamnesys.controller.dto.LinkDTO;
import com.anamnesys.controller.dto.RecordRequest;
import com.anamnesys.controller.dto.RecordResponse;
import com.anamnesys.controller.dto.SendRecordRequest;
import com.anamnesys.domain.SendRecord;
import com.anamnesys.repository.model.RecordModel;
import com.anamnesys.service.RecordService;
import com.anamnesys.util.RecordMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
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

    @DeleteMapping("/{recordId}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long userId, @PathVariable Long recordId) {

        logger.info("Received request to delete record: {}", recordId);
        recordService.deleteRecord(userId, recordId);
        logger.info("Record delete successfully with ID: {} ", recordId);

        return new ResponseEntity<>(HttpStatus.OK);
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
            @PageableDefault Pageable pageable) {

        logger.info("Received request get all records for userId: {}", userId);
        Page<RecordModel> records = recordService.getRecordsByUserId(userId, pageable);

        Page<RecordResponse> response = records.map(RecordMapper::toRecordNotQuestionsResponse);
        logger.info("Process get all records for userId: {} ", userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("find")
    public ResponseEntity<List<RecordResponse>> getRecordByName(
            @PathVariable Long userId,
            @RequestParam String name) {

        logger.info("Received request get record by name: {} {}", userId, name);
        List<RecordModel> records = recordService.getRecordByName(name, userId);

        List<RecordResponse> response = records.stream()
                .map(RecordMapper::toRecordNotQuestionsResponse)
                .toList();
        logger.info("Process get record by name: {} {}", userId, name);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/send")
    public ResponseEntity<LinkDTO> sendRecord(@PathVariable Long userId, @Valid @RequestBody SendRecordRequest sendRecordRequest) throws JsonProcessingException {

        logger.info("Received request send record: {} {}", userId, sendRecordRequest);
        SendRecord sendRecord = RecordMapper.toSendRecord(sendRecordRequest, userId);
        LinkDTO linkDTO = recordService.sendRecord(sendRecord);
        logger.info("Record send successfully with ID: {} ", sendRecord.getId());
        return ResponseEntity.ok(linkDTO);
    }

    @GetMapping("formFiller/{recordId}")
    public ResponseEntity<RecordResponse> formFiller(
            @PathVariable Long userId,
            @PathVariable Long recordId) {

        logger.info("Received request formFiller user and record: {} {}", userId, recordId);
        RecordResponse recordResponse = recordService.formFiller(recordId, userId);
        logger.info("Process formFiller user and record: {} {}", userId, recordId);
        return ResponseEntity.ok(recordResponse);
    }

    @GetMapping("/form-data/{linkId}")
    public ResponseEntity<RecordResponse> getFormData(@PathVariable Long userId, @PathVariable String linkId) {
        logger.info("Received get Form Data: {}", linkId);
        RecordResponse response = recordService.getFormData(linkId);
        logger.info("successfully get Form Data: {}", linkId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("formFiller/submit-form/{recordId}/clientId/{clientId}")
    public ResponseEntity<Void> submitFormFiller(@PathVariable Long recordId,
                                                 @PathVariable Long userId,
                                                 @PathVariable Long clientId,
                                                 @RequestBody String formResponses) {

        logger.info("Received submit formfiller - recordId {} ", recordId);
        recordService.saveAnswerFormFiller(recordId, userId, clientId, formResponses);
        logger.info("Answer saved formfiller - {} ", recordId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PostMapping("/submit-form/{linkId}")
    public ResponseEntity<Void> submitForm(@PathVariable String linkId, @RequestBody String formResponses,
                                           @PathVariable Long userId) {

        logger.info("Received answer save: {}", linkId);
        recordService.saveAnswer(linkId, formResponses, userId);
        logger.info("Answer saved successfully: {}", linkId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
