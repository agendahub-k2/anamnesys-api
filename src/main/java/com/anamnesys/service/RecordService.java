package com.anamnesys.service;

import com.anamnesys.exception.RecordNotFoundException;
import com.anamnesys.repository.RecordRepository;
import com.anamnesys.repository.model.QuestionModel;
import com.anamnesys.repository.model.RecordModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordService {

    @Autowired
    RecordRepository repository;
    @Autowired
    UserService userService;

    public void createRecord(RecordModel model) {

        validatedUser(model.getUserId());
        validatedRecord(model);
        repository.save(model);
    }

    public RecordModel updateRecord(RecordModel model) {

        validatedUser(model.getUserId());

        RecordModel modelDataBase = repository.findById(model.getId()).orElseThrow(RecordNotFoundException::new);
        setValues(model, modelDataBase);

        repository.save(modelDataBase);
        return modelDataBase;
    }

    public Page<RecordModel> getRecordsByUserId(Long userId, Pageable pageable) {
        return repository.findByUserId(userId, pageable);
    }

    public RecordModel getRecordById(Long recordId, Long userId) {
        validatedUser(userId);
        return repository.findById(recordId).orElseThrow(RecordNotFoundException::new);
    }

    private void setValues(RecordModel model, RecordModel modelDataBase) {
        modelDataBase.setName(model.getName());

        List<QuestionModel> existingQuestions = modelDataBase.getQuestions();
        existingQuestions.clear();

        model.getQuestions().forEach(question -> {
            question.setRecord(modelDataBase);
            existingQuestions.add(question);
        });
    }

    private void validatedRecord(RecordModel model) {
        if (repository.existsByName(model.getName())) {
            throw new IllegalArgumentException("Já existe um registro com o nome fornecido.");
        }
    }

    private void validatedUser(Long userId) {
        userService.getUser(userId);
    }
}
