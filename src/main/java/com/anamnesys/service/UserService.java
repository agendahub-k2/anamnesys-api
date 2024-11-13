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

        setPasswordEncrypt(model);
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
        userDataBase.setCategory(model.getCategory());
        userDataBase.setName(model.getName());
        userDataBase.setPhone(model.getPhone());
        userDataBase.setPassword(model.getPassword());
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
