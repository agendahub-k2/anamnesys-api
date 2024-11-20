package com.anamnesys.service;

import com.anamnesys.exception.PatientNotFoundException;
import com.anamnesys.repository.PatientRepository;
import com.anamnesys.repository.model.PatientModel;
import org.springframework.beans.factory.annotation.Autowired;
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

    private void updateValues(PatientModel patientModelDataBase, PatientModel model) {
        patientModelDataBase.setName(model.getName());
        patientModelDataBase.setEmail(model.getEmail());
        patientModelDataBase.setPhone(model.getPhone());
    }

    private void validatedUser(PatientModel model) {
        userService.getUser(model.getUserId());
    }
}