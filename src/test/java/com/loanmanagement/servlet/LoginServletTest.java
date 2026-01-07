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

    private LoginServlet loginServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter stringWriter;

    @BeforeEach
    void setup() throws Exception {

        loginServlet = new LoginServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        stringWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
    }

   
    @Test
    void testInvalidJson() throws Exception {

        ByteArrayInputStream bais =
                new ByteArrayInputStream("invalid-json".getBytes());

        ServletInputStream inputStream = new ServletInputStream() {

            @Override
            public int read() {
                return bais.read();
            }

            @Override
            public boolean isFinished() {
                return bais.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }
        };

        when(request.getInputStream()).thenReturn(inputStream);

        loginServlet.doPost(request, response);

        assertEquals("Invalid JSON request", stringWriter.toString());
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

  
    @Test
    void testMissingFields() throws Exception {

        String json = "{\"username\":null,\"password\":null}";
        ByteArrayInputStream bais =
                new ByteArrayInputStream(json.getBytes());

        when(request.getInputStream()).thenReturn(new ServletInputStream() {
            @Override public int read() { return bais.read(); }
            @Override public boolean isFinished() { return bais.available() == 0; }
            @Override public boolean isReady() { return true; }
            @Override public void setReadListener(ReadListener readListener) {}
        });

        loginServlet.doPost(request, response);

        assertEquals("Username and password required", stringWriter.toString());
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }


    @Test
    void testInvalidCredentials() throws Exception {

        String json = "{\"username\":\"bad\",\"password\":\"bad\"}";
        ByteArrayInputStream bais =
                new ByteArrayInputStream(json.getBytes());

        when(request.getInputStream()).thenReturn(new ServletInputStream() {
            @Override public int read() { return bais.read(); }
            @Override public boolean isFinished() { return bais.available() == 0; }
            @Override public boolean isReady() { return true; }
            @Override public void setReadListener(ReadListener readListener) {}
        });

        loginServlet.doPost(request, response);

        assertEquals("Invalid credentials", stringWriter.toString());
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

  
    @Test
    void testLoginSuccessResponseFormat() throws Exception {

        String json = "{\"username\":\"user\",\"password\":\"pass\"}";
        ByteArrayInputStream bais =
                new ByteArrayInputStream(json.getBytes());

        when(request.getInputStream()).thenReturn(new ServletInputStream() {
            @Override public int read() { return bais.read(); }
            @Override public boolean isFinished() { return bais.available() == 0; }
            @Override public boolean isReady() { return true; }
            @Override public void setReadListener(ReadListener readListener) {}
        });

        loginServlet.doPost(request, response);

        verify(response).setStatus(anyInt());
    }
}
