package com.anamnesys.service;


import com.anamnesys.controller.dto.InfoDashboard;
import com.anamnesys.controller.dto.LinkDTO;
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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.MessageFormat;
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
    @Autowired
    EmailService emailService;
    @Value("${spring.mail.username}")
    private String from;
    @Value("${form.base.url}")
    String formBaseUrl;


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
            userModel.setUpdateAt(LocalDateTime.now());
            setPasswordEncrypt(userModel);
            userRepository.updatePasswordByEmailAndId(userModel.getPassword(), LocalDateTime.now(), userModel.getEmail(), userId);
        } else {
            throw new EmailOrPasswordException();
        }
    }

    @Transactional
    public void resetPasswordByToken(String newPassword, String token) {
        DecodedJWT decodedJWT = JwtUtil.verifyTokenByResetPassword(token);
        boolean tokenExpired = JwtUtil.isTokenExpired(decodedJWT);
        if(!tokenExpired){
            System.out.println("TOKEN EXPIRADO");
        }
        String email = decodedJWT.getSubject();
        UserModel userModel = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND + email));
        userModel.setPassword(newPassword);
        userModel.setUpdateAt(LocalDateTime.now());
        setPasswordEncrypt(userModel);
        userRepository.updatePasswordByEmailAndId(userModel.getPassword(), LocalDateTime.now(), userModel.getEmail(), userModel.getId());
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
                        (String) row[6],   // patientName
                        (String) row[7],   // id link
                        row[8] != null ? ((Timestamp) row[8]).toLocalDateTime() : null  // returnDt
                )
        ).getContent();

        return new PageImpl<>(
                infosDashboard,
                pageable,
                records.getTotalElements()
        );
    }

    public void sendResetPassword(String email) {
        UserModel user = userRepository.findByEmail(email).orElseThrow(EmailOrPasswordException::new);
        String token = JwtUtil.generateResetPassWordToken(user.getEmail());
        String link = formBaseUrl + "reset_password?token=" + token;

        String emailBody = Constants.EMAIL_TEMPLATE_RESET
                .replace("{0}", user.getName())
                .replace("{1}", link);

        emailService.enviarEmail(user.getEmail(), Constants.MESSAGE_RESET, emailBody);
    }


    private void setPasswordEncrypt(UserModel user) {
        user.setPassword(passwordService.hashPassword(user.getPassword()));
    }
}
