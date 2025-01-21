package com.anamnesys.service;


import com.anamnesys.controller.dto.InfoDashboard;
import com.anamnesys.domain.UserAuthenticated;
import com.anamnesys.exception.EmailOrPasswordException;
import com.anamnesys.exception.UnauthorizedException;
import com.anamnesys.exception.UserNotFoundException;
import com.anamnesys.repository.RecordSendRepository;
import com.anamnesys.repository.UserRepository;
import com.anamnesys.repository.model.UserModel;
import com.anamnesys.util.Constants;
import com.anamnesys.util.JwtUtil;
import com.anamnesys.util.STATUS;
import com.anamnesys.util.UserMapper;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static com.anamnesys.util.Constants.USER_NOT_FOUND;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordService passwordService;
    @Autowired
    RecordSendRepository recordSendRepository;


    public UserModel createUser(UserModel model) {
        setPasswordEncrypt(model);
        return userRepository.save(model);
    }

    public UserModel updateUser(UserModel model) {

        UserModel userDataBase = getUser(model.getId());
        setUpdateUser(userDataBase, model);
        return userRepository.save(userDataBase);
    }

    public UserModel getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new UserNotFoundException(USER_NOT_FOUND)
        );
    }

    private void setUpdateUser(UserModel userDataBase, UserModel model) {
        userDataBase.setName(model.getName());
        userDataBase.setPhone(model.getPhone());
        userDataBase.setEmail(model.getEmail());
        userDataBase.setUpdateAt(LocalDateTime.now());
    }

    public void inactiveUser(Long id) {
        UserModel user = getUser(id);
        user.setStatus(STATUS.INACTIVE.name());
        userRepository.save(user);
    }

    public UserAuthenticated login(String email, String password) {
        UserAuthenticated userAuthenticated;

        UserModel userModel = userRepository.findByEmail(email).orElseThrow(EmailOrPasswordException::new);

        boolean isValid = passwordService.checkPassword(password, userModel.getPassword());
        if (isValid) {
            userAuthenticated = UserMapper.toUserAuthenticated(JwtUtil.generateToken(email), userModel);
        } else {
            throw new EmailOrPasswordException();
        }

        return userAuthenticated;
    }

    @Transactional
    public void resetPassword(Long userId, String oldPassword, String newPassword) {

        UserModel userModel = userRepository.findById(userId).orElseThrow(EmailOrPasswordException::new);

        boolean isValid = passwordService.checkPassword(oldPassword, userModel.getPassword());

        if (isValid) {
            userModel.setPassword(newPassword);
            setPasswordEncrypt(userModel);
            userRepository.updatePasswordByEmailAndId(userModel.getPassword(), LocalDateTime.now(), userModel.getEmail(), userId);
        } else {
            throw new EmailOrPasswordException();
        }
    }

    public UserModel getUserByToken(String token) {
        try {
            DecodedJWT decodedJWT = JwtUtil.verifyToken(token);
            String emailToken = decodedJWT.getSubject();
            return userRepository.findByEmail(emailToken).orElseThrow(() -> new UserNotFoundException(Constants.USER_NOT_FOUND + emailToken));
        } catch (JWTDecodeException ex) {
            throw new UnauthorizedException(token);
        }
    }

    public Page<InfoDashboard> getDashboard(Long userId, Pageable pageable) {

        if (pageable.getSort().isEmpty()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Order.desc("created_at")));
        }

        Page<Object[]> records = recordSendRepository.findSendRecordsByUserIdAndClientId(
                userId, pageable
        );

        List<InfoDashboard> infosDashboard = records.map(row ->
                new InfoDashboard(
                        (String) row[0],   // status
                        (Long) row[1],     // clientId
                        (String) row[2],   // recordName
                        ((Timestamp) row[3]).toLocalDateTime(), // createdAt
                        (boolean) row[4],  // email
                        (boolean) row[5],  // whatsapp
                        (String) row[6] ,   // patientName
                        (String) row[7]    // id link
                )
        ).getContent();

        return new PageImpl<>(
                infosDashboard,
                pageable,
                records.getTotalElements()
        );
    }

    private void setPasswordEncrypt(UserModel user) {
        user.setPassword(passwordService.hashPassword(user.getPassword()));
    }
}
