package com.anamnesys.service;

import com.anamnesys.exception.PatientNotFoundException;
import com.anamnesys.repository.PatientRepository;
import com.anamnesys.repository.model.PatientModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PatientService {

    @Autowired
    PatientRepository patientRepository;
    @Autowired
    UserService userService;

    public void createPatient(PatientModel model) {

        validatedUser(model);

        patientRepository.save(model);
    }

    public void updatePatient(PatientModel model) {

        validatedUser(model);

        PatientModel patientModelDataBase = patientRepository.findById(model.getId()).orElseThrow(PatientNotFoundException::new);

        updateValues(patientModelDataBase, model);

        patientRepository.save(model);
    }

    public Page<PatientModel> getAllPatientsByUserId(Long userId, Pageable pageable) {
        return patientRepository.findByUserId(userId, pageable);
    }

    public Page<PatientModel> getAllPatientsByUserIdAndName(Long userId, String name, Pageable pageable) {
        return patientRepository.findByUserIdAndNameContainingIgnoreCase(userId, name, pageable);
    }

    public PatientModel getPatientById(Long patientId) {
        return patientRepository.findById(patientId).orElseThrow(PatientNotFoundException::new);
    }

    private void updateValues(PatientModel patientModelDataBase, PatientModel model) {
        patientModelDataBase.setName(model.getName());
        patientModelDataBase.setEmail(model.getEmail());
        patientModelDataBase.setPhone(model.getPhone());
    }

    private void validatedUser(PatientModel model) {
        userService.getUser(model.getUserId());
    }
}
