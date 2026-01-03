package com.loanmanagement.service;

import com.loanmanagement.dao.AuthDao;
import com.loanmanagement.util.PasswordUtil;
import com.loanmanagement.util.TokenUtil;

public class AuthService {

    private AuthDao authDao = new AuthDao();

    public String login(String username, String password) {

        if (username == null || password == null) {
            throw new IllegalArgumentException("Username and password required");
        }

        String hashedPassword = PasswordUtil.hash(password);

        long customerId = authDao.validateLogin(username, hashedPassword);

        if (customerId == 0) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        String token = TokenUtil.generateToken(username);
        authDao.saveToken(customerId, token);

        return token;
    }
}
