package com.anamnesys.service;

import com.anamnesys.domain.SendRecord;
import com.anamnesys.exception.RecordNotFoundException;
import com.anamnesys.repository.RecordRepository;
import com.anamnesys.repository.RecordSendRepository;
import com.anamnesys.repository.model.QuestionModel;
import com.anamnesys.repository.model.RecordModel;
import com.anamnesys.repository.model.RecordSendModel;
import com.anamnesys.repository.model.TemplateModel;
import com.anamnesys.util.RecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecordService {

    @Autowired
    RecordRepository repository;
    @Autowired
    RecordSendRepository recordSendRepository;
    @Autowired
    UserService userService;
    @Autowired
    TemplateService templateService;

    public void createRecord(RecordModel model) {
        validatedUser(model.getUserId());
        validatedRecord(model);
        setOptions(model);
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

    public List<RecordModel> getRecordByName(String name, Long userId) {
        validatedUser(userId);
        return repository.findByUserIdAndNameContaining(userId, name);
    }


    public RecordModel getFormData(String linkId) {
        try {
            RecordSendModel recordSendModel = recordSendRepository.getReferenceById(linkId);
            return this.getRecordById(recordSendModel.getRecordId(), recordSendModel.getUserId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendRecord(SendRecord sendRecord) {

        try {
            validateSendRecord(sendRecord);
            RecordSendModel sendModel = RecordMapper.toSendModel(sendRecord);
            recordSendRepository.save(sendModel);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void validateSendRecord(SendRecord sendRecord) {
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
        if (repository.existsByNameAndUserId(model.getName(), model.getUserId())) {
            throw new IllegalArgumentException("Já existe um registro com o nome fornecido.");
        }
    }

    private void setOptions(RecordModel model) {
        model.getQuestions().forEach(questionModel -> {
            switch (questionModel.getQuestionType()) {
                case SELECT:
                    questionModel.setOptions(String.join(";", questionModel.getOptions()));
                    break;
                case BOOLEAN:

                    if (questionModel.getOptions().isEmpty()) {
                        questionModel.setOptions("SIM;NÃO");
                    }
                    break;
                default:
                    break;
            }
        });
    }

    private void setQuestionsTemplate(RecordModel model) {
        try {
            TemplateModel template = templateService.getTemplateById(model.getTemplateId());
            List<QuestionModel> questionsFromTemplate = template.getQuestions();

            questionsFromTemplate.forEach(question -> {
                QuestionModel questionModel = new QuestionModel();
                questionModel.setQuestion(question.getQuestion());
                questionModel.setSection(question.getSection());
                questionModel.setDescriptionSection(question.getDescriptionSection());
                questionModel.setIsRequired(question.getIsRequired());
                questionModel.setQuestionType(question.getQuestionType());
                questionModel.setRecord(model);
                model.addQuestion(questionModel);
            });
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to set questions template", e);
        }
    }

    private void validatedUser(Long userId) {
        userService.getUser(userId);
    }

}
