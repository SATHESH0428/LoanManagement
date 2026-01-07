package com.loanmanagement.servlet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
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

class CustomerServletTest {

    private CustomerServlet customerServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter stringWriter;
    private PrintWriter writer;

    @BeforeEach
    void setUp() throws Exception {

        customerServlet = new CustomerServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    void testDoPostFailure() throws Exception {

        when(request.getParameter("customerCode")).thenReturn("");
        when(request.getParameter("name")).thenReturn("Test");

        customerServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }


    @Test
    void testDoGetInvalidId() throws Exception {

        when(request.getParameter("id")).thenReturn("abc");

        customerServlet.doGet(request, response);

        assertEquals("Invalid customer id", stringWriter.toString());
    }

    @Test
    void testDoGetAllSuccess() throws Exception {

        when(request.getParameter("id")).thenReturn(null);

        customerServlet.doGet(request, response);

        verify(response).setContentType("application/json");
    }

    @Test
    void testDoPutFailure() throws Exception {

        String invalidJson = "{invalid-json}";

        ByteArrayInputStream bais =
                new ByteArrayInputStream(invalidJson.getBytes());

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

        customerServlet.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }


    @Test
    void testDoDeleteInvalidId() throws Exception {

        when(request.getParameter(anyString())).thenReturn("xyz");

        customerServlet.doDelete(request, response);

        assertEquals("Invalid customer id", stringWriter.toString());
    }
}
