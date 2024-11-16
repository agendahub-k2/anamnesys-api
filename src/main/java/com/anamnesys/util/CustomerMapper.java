package com.anamnesys.util;

import com.anamnesys.controller.dto.*;
import com.anamnesys.domain.UserAuthenticated;
import com.anamnesys.repository.model.CustomerModel;
import com.anamnesys.repository.model.UserModel;

public class CustomerMapper {

    public static CustomerModel toModel(CustomerRequest request, Long customerId, Long userId) {
        CustomerModel customerModel = new CustomerModel();
        customerModel.setId(customerId);
        customerModel.setName(request.getName());
        customerModel.setPhone(request.getPhone());
        customerModel.setEmail(request.getEmail());
        customerModel.setUserId(userId);
        return customerModel;
    }

    public static CustomerResponse toUserResponse(CustomerModel model) {
        CustomerResponse response = new CustomerResponse();
        response.setName(model.getName());
        response.setEmail(model.getEmail());
        response.setPhone(model.getPhone());
        response.setId(model.getId());
        response.setUpdateAt(model.getUpdateAt().toString());
        response.setCreatedAt(model.getCreatedAt().toString());
        response.setCreatedAt(model.getCreatedAt().toString());
        response.setUserId(model.getUserId());
        return response;
    }
}
