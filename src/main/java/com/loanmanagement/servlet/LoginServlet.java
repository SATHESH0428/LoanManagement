package com.loanmanagement.servlet;

import java.io.IOException;
import java.io.Serial;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loanmanagement.model.LoginRequest;
import com.loanmanagement.service.AuthService;

@WebServlet("/api/login")
public class LoginServlet extends HttpServlet {

    @Serial
    private static final long serialVersionUID = 1L;

  
    private static final String JSON_TYPE = "application/json";
    private static final String INVALID_JSON = "Invalid JSON request";
    private static final String MISSING_FIELDS = "Username and password required";
    private static final String INVALID_CREDENTIALS = "Invalid credentials";

    private static final Logger LOG =
            LoggerFactory.getLogger(LoginServlet.class);

  
    private static final AuthService authService = new AuthService();
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType(JSON_TYPE);

        LoginRequest loginRequest;

        try {
            loginRequest =
                    mapper.readValue(request.getInputStream(), LoginRequest.class);
        } catch (Exception e) {
            LOG.error("Login request JSON is invalid", e);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(INVALID_JSON);
            return;
        }

        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        if (username == null || password == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(MISSING_FIELDS);
            return;
        }

        try {
            String token = authService.login(username, password);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"token\":\"" + token + "\"}");
        } catch (Exception e) {
            LOG.warn("Login failed for user: {}", username);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(INVALID_CREDENTIALS);
        }
    }
}
