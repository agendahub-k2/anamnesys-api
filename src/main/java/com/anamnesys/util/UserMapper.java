package com.anamnesys.util;

import com.anamnesys.controller.dto.LoginResponse;
import com.anamnesys.controller.dto.UserRequest;
import com.anamnesys.controller.dto.UserResponse;
import com.anamnesys.controller.dto.UserUpdatedRequest;
import com.anamnesys.domain.UserAuthenticated;
import com.anamnesys.repository.model.UserModel;

public class UserMapper {

    public static UserModel toModel(UserRequest request, Long id) {
        UserModel userModel = new UserModel();
        userModel.setCategory(request.getCategory());
        userModel.setName(request.getName());
        userModel.setEmail(request.getEmail());
        userModel.setPassword(request.getPassword());
        userModel.setPhone(request.getPhone());
        userModel.setId(id);
        userModel.setStatus(STATUS.ACTIVE.name());
        return userModel;
    }

    public static UserModel toUpdateModel(UserUpdatedRequest request, Long id) {
        UserModel userModel = new UserModel();
        userModel.setName(request.getName());
        userModel.setEmail(request.getEmail());
        userModel.setPhone(request.getPhone());
        userModel.setId(id);
        return userModel;
    }


    public static UserResponse toUserResponse(UserModel user) {
        UserResponse response = new UserResponse();
        response.setCategory(user.getCategory());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setId(user.getId());
        response.setUpdateAt(user.getUpdateAt().toString());
        response.setCreatedAt(user.getCreatedAt().toString());
        response.setStatus(STATUS.valueOf(user.getStatus()));
        return response;
    }

    public static UserAuthenticated toUserAuthenticated(String token, UserModel userModel) {
        UserAuthenticated userAuthenticated = new UserAuthenticated();
        userAuthenticated.setToken(token);
        userAuthenticated.setId(userModel.getId());
        userAuthenticated.setName(userModel.getName());
        userAuthenticated.setEmail(userModel.getEmail());
        userAuthenticated.setPhone(userModel.getPhone());
        userAuthenticated.setCreatedAt(userModel.getCreatedAt());
        userAuthenticated.setUpdateAt(userModel.getUpdateAt());
        userAuthenticated.setCategory(userModel.getCategory());
        userAuthenticated.setStatus(userModel.getStatus());
        return userAuthenticated;
    }

    public static LoginResponse toLoginResponse(UserAuthenticated user) {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(user.getToken());
        loginResponse.setId(user.getId());
        loginResponse.setName(user.getName());
        loginResponse.setEmail(user.getEmail());
        loginResponse.setCreatedAt(user.getCreatedAt().toString());
        loginResponse.setUpdateAt(user.getUpdateAt().toString());
        loginResponse.setPhone(user.getPhone());
        loginResponse.setCategory(user.getCategory());
        loginResponse.setStatus(STATUS.valueOf(user.getStatus()));
        return loginResponse;
    }
}
