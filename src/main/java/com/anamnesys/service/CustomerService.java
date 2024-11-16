package com.anamnesys.service;

import com.anamnesys.exception.CustomerNotFoundException;
import com.anamnesys.repository.CustomerRepository;
import com.anamnesys.repository.model.CustomerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    UserService userService;

    public void createCustomer(CustomerModel model) {

        validatedUser(model);

        customerRepository.save(model);
    }

    public void updateCustomer(CustomerModel model) {

        validatedUser(model);

        CustomerModel customerModelDataBase = customerRepository.findById(model.getId()).orElseThrow(CustomerNotFoundException::new);

        updateValues(customerModelDataBase, model);

        customerRepository.save(model);
    }

    private void updateValues(CustomerModel customerModelDataBase, CustomerModel model) {
        customerModelDataBase.setName(model.getName());
        customerModelDataBase.setEmail(model.getEmail());
        customerModelDataBase.setPhone(model.getPhone());
    }

    private void validatedUser(CustomerModel model) {
        userService.getUser(model.getUserId());
    }
}
