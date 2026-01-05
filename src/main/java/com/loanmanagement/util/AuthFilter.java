package com.loanmanagement.util;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.loanmanagement.service.AuthService;

@WebFilter("/api/*")
public class AuthFilter implements Filter {

    private AuthService authService;

    @Override
    public void init(FilterConfig filterConfig) {
        authService = new AuthService();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();

       
        if (path.endsWith("/api/login")) {
            chain.doFilter(request, response);
            return;
        }


        String token = req.getHeader("Authorization");

        if (token == null || token.isEmpty()) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("Missing authentication token");
            return;
        }

        // Validate token
        boolean valid = authService.validateToken(token);

        if (!valid) {
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("Invalid or expired token");
            return;
        }

        // Token valid → continue
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        
    }
}

