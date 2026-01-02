package com.loanmanagement.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loanmanagement.model.Customer;
import com.loanmanagement.servelet.CustomerServlet;
import com.loanmanagement.service.CustomerService;
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
import java.lang.reflect.Field;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CustomerServletTest {

    ObjectMapper objectMapper = new ObjectMapper();
    private CustomerServlet customerServlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private CustomerService customerService;

    @BeforeEach
    void setUp() throws Exception {
        customerServlet = new CustomerServlet();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        customerService = mock(CustomerService.class);

        Field field = CustomerServlet.class.getDeclaredField("customerService");
        field.setAccessible(true);
        field.set(customerServlet, customerService);
    }

  
    @Test
    void testDoPost() throws Exception {

        when(request.getParameter("customerCode")).thenReturn("C001");
        when(request.getParameter("name")).thenReturn("Sathesh");
        when(request.getParameter("email")).thenReturn("test@mail.com");
        when(request.getParameter("mobile")).thenReturn("9876543210");
        when(request.getParameter("address")).thenReturn("Chennai");
        when(request.getParameter("kycStatus")).thenReturn("ACTIVE");

        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        customerServlet.doPost(request, response);

        verify(customerService).createCustomer(any(Customer.class));
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

   
    @Test
    void testDoPostInvalid() throws Exception {

        when(request.getParameter("customerCode")).thenThrow(RuntimeException.class);

        try {
            customerServlet.doPost(request, response);
        } catch (Exception e) {
            verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }


    @Test
    void testDoGet() throws Exception {

        when(customerService.getAllCustomers())
                .thenReturn(List.of(new Customer()));

        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        customerServlet.doGet(request, response);

        verify(customerService).getAllCustomers();
    }

   
    @Test
    void testDoGetById() throws Exception {

        when(request.getParameter("id")).thenReturn("1");
        when(customerService.getCustomerById(1L))
                .thenReturn(new Customer());

        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        customerServlet.doGet(request, response);

        verify(customerService).getCustomerById(1L);
    }


    @Test
    void testDoPut() throws Exception {

        Customer customer = new Customer();
        customer.setCustomerCode("C002");

        String json = objectMapper.writeValueAsString(customer);
        when(request.getInputStream()).thenReturn(inputStream(json));

      
        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        customerServlet.doPut(request, response);

        verify(customerService).updateCustomer(any(Customer.class));
    }

 
    @Test
    void testDoDelete() throws Exception {

        when(request.getParameter("id")).thenReturn("1");

        customerServlet.doDelete(request, response);

        verify(customerService).deleteCustomer(1L);
    }

 
   private ServletInputStream inputStream(String json) {
        ByteArrayInputStream bis = new ByteArrayInputStream(json.getBytes());
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return bis.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setReadListener(ReadListener readListener) {}

            @Override
            public int read() throws IOException {
                return bis.read();
            }
        };
    }
}
