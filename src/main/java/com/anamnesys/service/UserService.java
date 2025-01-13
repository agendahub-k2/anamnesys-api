package com.anamnesys.service;


import com.anamnesys.domain.UserAuthenticated;
import com.anamnesys.exception.EmailOrPasswordException;
import com.anamnesys.exception.UnauthorizedException;
import com.anamnesys.exception.UserNotFoundException;
import com.anamnesys.repository.UserRepository;
import com.anamnesys.repository.model.UserModel;
import com.anamnesys.util.Constants;
import com.anamnesys.util.JwtUtil;
import com.anamnesys.util.STATUS;
import com.anamnesys.util.UserMapper;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.anamnesys.util.Constants.USER_NOT_FOUND;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordService passwordService;

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

    private void setPasswordEncrypt(UserModel user) {
        user.setPassword(passwordService.hashPassword(user.getPassword()));
    }
}
