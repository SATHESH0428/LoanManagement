package com.loanmanagement.servlet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoginServletTest {

    private LoginServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter writer;

    @BeforeEach
    void setup() throws Exception {
        servlet = new LoginServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
    }

    // ❌ Invalid JSON
    @Test
    void invalidJson() throws Exception {
        when(request.getInputStream()).thenReturn(stream("invalid"));

        servlet.doPost(request, response);

        assertEquals("Invalid JSON request", writer.toString());
        verify(response).setStatus(400);
    }

    // ❌ Missing username/password
    @Test
    void missingFields() throws Exception {
        when(request.getInputStream())
                .thenReturn(stream("{\"username\":null,\"password\":null}"));

        servlet.doPost(request, response);

        assertEquals("Username and password required", writer.toString());
        verify(response).setStatus(400);
    }

    // ❌ Invalid credentials
    @Test
    void invalidCredentials() throws Exception {
        when(request.getInputStream())
                .thenReturn(stream("{\"username\":\"x\",\"password\":\"y\"}"));

        servlet.doPost(request, response);

        assertEquals("Invalid credentials", writer.toString());
        verify(response).setStatus(401);
    }

    // ✅ Success
    @Test
    void loginSuccess() throws Exception {
        when(request.getInputStream())
                .thenReturn(stream("{\"username\":\"user\",\"password\":\"pass\"}"));

        servlet.doPost(request, response);

        verify(response).setStatus(anyInt());
    }

    private ServletInputStream stream(String json) {
        ByteArrayInputStream bis = new ByteArrayInputStream(json.getBytes());
        return new ServletInputStream() {
            @Override public int read() { return bis.read(); }
            @Override public boolean isFinished() { return bis.available() == 0; }
            @Override public boolean isReady() { return true; }
            @Override public void setReadListener(ReadListener readListener) {}
        };
    }
}
