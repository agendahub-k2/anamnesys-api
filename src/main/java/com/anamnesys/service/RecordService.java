package com.anamnesys.service;

import com.anamnesys.controller.dto.LinkDTO;
import com.anamnesys.controller.dto.RecordResponse;
import com.anamnesys.controller.dto.TermResponse;
import com.anamnesys.domain.STATUS_RECORD;
import com.anamnesys.domain.SendRecord;
import com.anamnesys.exception.AnswerExistingException;
import com.anamnesys.exception.RecordNotFoundException;
import com.anamnesys.repository.AnswerRepository;
import com.anamnesys.repository.RecordRepository;
import com.anamnesys.repository.RecordSendRepository;
import com.anamnesys.repository.TermRepository;
import com.anamnesys.repository.model.*;
import com.anamnesys.util.Constants;
import com.anamnesys.util.RecordMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;

import static com.anamnesys.util.Constants.EMAIL_TEMPLATE;

@Service
public class RecordService {

    @Autowired
    RecordRepository repository;
    @Autowired
    RecordSendRepository recordSendRepository;
    @Autowired
    UserService userService;
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    TermRepository termRepository;
    @Autowired
    EmailService emailService;
    @Autowired
    PatientService patientService;
    @Value("${form.base.url}")
    String formBaseUrl;

    public void createRecord(RecordModel model) {
        validatedUser(model.getUserId());
        validatedRecord(model);
        setOptions(model);
        repository.save(model);
    }

    @Transactional
    public void deleteRecord(Long userId, Long recordId) {
        repository.deleteByIdAndUserId(recordId, userId);
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


    public RecordResponse getFormData(String linkId) {
        try {
            TermModel termModel;
            TermResponse termResponse = null;

            RecordSendModel recordSendModel = recordSendRepository.getReferenceById(linkId);
            if (recordSendModel.getStatus() == STATUS_RECORD.RECEBIDO) {
                throw new AnswerExistingException("Ficha já enviada.");
            }
            RecordModel recordById = this.getRecordById(recordSendModel.getRecordId(), recordSendModel.getUserId());
            if (recordById.getTermId() != null) {
                termModel = termRepository.getReferenceById(recordById.getTermId());
                termResponse = new TermResponse();
                termResponse.setId(termModel.getId());
                termResponse.setTerm(termModel.getTerm());
                termResponse.setName(termModel.getName());
            }

            RecordResponse recordResponse = RecordMapper.toRecordResponse(recordById);
            recordResponse.setTerm(termResponse);

            return recordResponse;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public LinkDTO sendRecord(SendRecord sendRecord) {
        try {
            String linkId = sendRecord.getUserId() + "/record/" + sendRecord.getId();
            String formUrl = formBaseUrl + linkId;

            if (sendRecord.getIsSendMail()) {
                PatientModel patient = patientService.getPatientById(sendRecord.getClientId());
                String recordNameById = getRecordNameById(sendRecord.getRecordId());
                String emailSubject = MessageFormat.format(Constants.MESSAGE_SEND_SUBJECT, recordNameById);
                String emailBody = EMAIL_TEMPLATE.replace("{name}", patient.getName())
                        .replace("{formUrl}", formUrl);
                emailService.enviarEmail(patient.getEmail(), emailSubject, emailBody);
            }

            RecordSendModel sendModel = RecordMapper.toSendModel(sendRecord);
            recordSendRepository.save(sendModel);

            return new LinkDTO(formUrl);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void saveAnswer(String linkId, String formResponses) {
        AnswerModel answerModel = new AnswerModel();
        answerModel.setId(linkId);
        answerModel.setAnswer(formResponses);

        boolean exists = answerRepository.existsById(linkId);

        if (exists) {
            throw new AnswerExistingException("Ficha já enviada.");
        }

        recordSendRepository.updateStatusAndUpdateAt(linkId, STATUS_RECORD.RECEBIDO);
        answerRepository.save(answerModel);
    }

    public List<RecordSendModel> getSendRecordsByUserIdPatientId(Long clientId, Long userId) {
        return recordSendRepository.findByUserIdAndClientId(userId, clientId);
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
                    if (questionModel.getOptions() == null || questionModel.getOptions().isEmpty()) {
                        questionModel.setOptions("SIM;NÃO");
                    }
                    break;
                default:
                    break;
            }
        });
    }

    private void validatedUser(Long userId) {
        userService.getUser(userId);
    }

    private String getRecordNameById(Long id) {
        return repository.findNameById(id)
                .orElseThrow(() -> new EntityNotFoundException("Registro não encontrado"));
    }
}
