package com.loanmanagement.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loanmanagement.model.Customer;
import com.loanmanagement.service.CustomerService;
import com.loanmanagement.servlet.CustomerServlet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CustomerServletTest {

    private CustomerServlet customerServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private CustomerService customerService;

    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws Exception {

        customerServlet = new CustomerServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        customerService = mock(CustomerService.class);

        var field = CustomerServlet.class.getDeclaredField("customerService");
        field.setAccessible(true);
        field.set(customerServlet, customerService);

        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
    }

    @Test
    void testDoPost() throws Exception {

        when(request.getParameter("customerCode")).thenReturn("CUST001");
        when(request.getParameter("name")).thenReturn("Sathesh");
        when(request.getParameter("email")).thenReturn("test@gmail.com");
        when(request.getParameter("mobile")).thenReturn("9876543210");
        when(request.getParameter("address")).thenReturn("Chennai");
        when(request.getParameter("kycStatus")).thenReturn("VERIFIED");

        customerServlet.doPost(request, response);

        verify(customerService).createCustomer(any(Customer.class));
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void testDoPostInvalid() throws Exception {

        when(request.getParameter("customerCode"))
                .thenThrow(RuntimeException.class);

        try {
            customerServlet.doPost(request, response);
        } catch (Exception ignored) {}

        verify(customerService, never()).createCustomer(any());
    }

    @Test
    void testDoGetAll() throws Exception {

        customerServlet.doGet(request, response);

        verify(customerService).getAllCustomers();
    }

    @Test
    void testDoGetById() throws Exception {

        when(request.getParameter("id")).thenReturn("1");

        customerServlet.doGet(request, response);

        verify(customerService).getCustomerById(1L);
    }

    @Test
    void testDoPut() throws Exception {

        Customer customer = new Customer();
        customer.setId(1);
        customer.setName("Updated Name");

        String json = mapper.writeValueAsString(customer);
        when(request.getInputStream()).thenReturn(inputStream(json));

        customerServlet.doPut(request, response);

        verify(customerService).updateCustomer(any(Customer.class));
    }

    @Test
    void testDoPutInvalid() throws Exception {

        when(request.getInputStream()).thenThrow(IOException.class);

        try {
            customerServlet.doPut(request, response);
        } catch (Exception ignored) {}

        verify(customerService, never()).updateCustomer(any());
    }

    @Test
    void testDoDelete() throws Exception {

        when(request.getParameter("id")).thenReturn("1");

        customerServlet.doDelete(request, response);

        verify(customerService).deleteCustomer(1L);
    }

    @Test
    void testDoDeleteInvalid() throws Exception {

        when(request.getParameter("id")).thenReturn(null);

        try {
            customerServlet.doDelete(request, response);
        } catch (Exception ignored) {}

        verify(customerService, never()).deleteCustomer(anyLong());
    }

    private ServletInputStream inputStream(String json) {

        ByteArrayInputStream bis = new ByteArrayInputStream(json.getBytes());

        return new ServletInputStream() {
            @Override public boolean isFinished() {
                return bis.available() == 0;
            }
            @Override public boolean isReady() {
                return true;
            }
            @Override public void setReadListener(ReadListener readListener) {}
            @Override public int read() throws IOException {
                return bis.read();
            }
        };
    }
}
