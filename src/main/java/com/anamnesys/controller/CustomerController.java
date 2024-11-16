package com.anamnesys.controller;

import com.anamnesys.controller.dto.CustomerRequest;
import com.anamnesys.controller.dto.CustomerResponse;
import com.anamnesys.repository.model.CustomerModel;
import com.anamnesys.service.CustomerService;
import com.anamnesys.util.CustomerMapper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("{userId}/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @PostMapping("/create")
    public ResponseEntity<CustomerResponse> createCustomer(@PathVariable Long userId, @Valid @RequestBody CustomerRequest customerRequest) {

        logger.info("Received request to create customer: {}", customerRequest);

        CustomerModel customerModel = CustomerMapper.toModel(customerRequest, null, userId);
        customerService.createCustomer(customerModel);

        logger.info("Customer created successfully with ID: {}", customerModel);

        return new ResponseEntity<>(CustomerMapper.toUserResponse(customerModel), HttpStatus.CREATED);
    }

    @PostMapping("/update/{customerId}")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable Long userId, @PathVariable Long customerId, @Valid @RequestBody CustomerRequest customerRequest) {

        logger.info("Received request to update customer: {}", customerRequest);

        CustomerModel customerModel = CustomerMapper.toModel(customerRequest, customerId, userId);
        customerService.updateCustomer(customerModel);

        logger.info("Customer updated successfully with ID: {}", customerModel);

        return new ResponseEntity<>(CustomerMapper.toUserResponse(customerModel), HttpStatus.CREATED);
    }
}
