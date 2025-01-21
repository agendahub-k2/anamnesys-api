package com.anamnesys.controller;

import com.anamnesys.controller.dto.*;
import com.anamnesys.domain.UserAuthenticated;
import com.anamnesys.exception.UnauthorizedException;
import com.anamnesys.repository.model.UserModel;
import com.anamnesys.service.UserService;
import com.anamnesys.util.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/create")
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest) {

        logger.info("Received request to create user: {}", userRequest);

        UserModel user = userService.createUser(UserMapper.toModel(userRequest, null));

        logger.info("User created successfully with ID: {}", user);

        return new ResponseEntity<>(UserMapper.toUserResponse(user), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdatedRequest userRequest) {

        logger.info("Received request to update user: {}", userRequest);

        UserModel user = userService.updateUser(UserMapper.toUpdateModel(userRequest, id));

        logger.info("User update successfully with ID: {}", user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {

        logger.info("Received request get user: {}", id);

        UserModel user = userService.getUser(id);

        logger.info("User get successfully with ID: {}", user);

        return new ResponseEntity<>(UserMapper.toUserResponse(user), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inactiveUser(@PathVariable Long id) {

        logger.info("Received request inactive user: {}", id);

        userService.inactiveUser(id);

        logger.info("User inactive successfully with ID: {}", id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {

        logger.info("Received request to login user: {}", loginRequest.getEmail());

        UserAuthenticated userAuthenticated = userService.login(loginRequest.getEmail(), loginRequest.getPassword());

        return ResponseEntity.ok(UserMapper.toLoginResponse(userAuthenticated));
    }

    @PostMapping("{userId}/reset_password")
    public ResponseEntity<Void> resetPassword(@PathVariable Long userId, @Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {

        logger.info("Received request to reset_password user: {}", userId);

        userService.resetPassword(userId, resetPasswordRequest.getOldPassword(), resetPasswordRequest.getNewPassword());

        logger.info("successfully reset_password user: {}", userId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("{userId}/dashboard")
    public ResponseEntity<PageDashboardResponse> getDashboard(@PathVariable Long userId, @PageableDefault Pageable pageable) {

        logger.info("Received request dashboard user: {}", userId);

        Page<InfoDashboard> dashboard = userService.getDashboard(userId, pageable);

        PageDashboardResponse response = new PageDashboardResponse();
        response.setContent(dashboard.getContent());
        response.setTotalPages(dashboard.getTotalPages());
        response.setTotalElements(dashboard.getTotalElements());
        response.setNumber(dashboard.getNumber());

        logger.info("successfully get dashboard user: {}", userId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/authenticate")
    public ResponseEntity<UserResponse> authenticate(HttpServletRequest request) {

        String tokenFromRequest = extractTokenFromRequest(request);
        if (tokenFromRequest == null) {
            throw new UnauthorizedException();
        }
        return ResponseEntity.ok(UserMapper.toUserResponse(userService.getUserByToken(tokenFromRequest)));
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
