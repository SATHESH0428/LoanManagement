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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.loanmanagement.service.AuthService;

@WebFilter("/api/*")
public class AuthFilter implements Filter {

    private static final Logger LOG =
            LoggerFactory.getLogger(AuthFilter.class);


    private static final AuthService authService = new AuthService();

    @Override
    public void init(FilterConfig filterConfig) {
        LOG.info("AuthFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI();

      
        if (path.endsWith("/api/login")) {
            chain.doFilter(request, response);
            return;
        }

        String token = req.getHeader("Authorization");

        if (token == null || token.isBlank()) {
            LOG.warn("Missing authentication token");
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("Missing authentication token");
            return;
        }

        boolean valid = authService.validateToken(token);

        if (!valid) {
            LOG.warn("Invalid or expired token");
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            res.getWriter().write("Invalid or expired token");
            return;
        }

        LOG.debug("Token validated successfully");
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
       
        LOG.info("AuthFilter destroyed");
    }
}
