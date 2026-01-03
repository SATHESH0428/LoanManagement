package com.loanmanagement.servelet;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.loanmanagement.service.AuthService;
@WebServlet("/login")
public class LoginServlet extends HttpServlet{
	private AuthService authService = new AuthService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            String token = authService.login(username, password);
            response.setContentType("application/json");
            response.getWriter().write("{\"token\":\"" + token + "\"}");

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(e.getMessage());
        }
    }

}
