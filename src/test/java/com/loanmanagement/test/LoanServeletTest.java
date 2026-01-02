package com.loanmanagement.test;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.loanmanagement.exception.DataException;
import com.loanmanagement.model.Loan;
import com.loanmanagement.servelet.LoanServelet;
import com.loanmanagement.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LoanServeletTest {

    private LoanServelet loanServelet;
    private LoanService loanService;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        loanServelet = new LoanServelet();
        loanService = mock(LoanService.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        objectMapper = new ObjectMapper();

        Field field = LoanServelet.class.getDeclaredField("loanService");
        field.setAccessible(true);
        field.set(loanServelet, loanService);
    }

  
    @Test
    void testDoPost() throws Exception {

        Loan loan = new Loan();
        loan.setLoanType("HOME");
        loan.setPrincipalAmount(BigDecimal.valueOf(50000));

        String json = objectMapper.writeValueAsString(loan);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));

        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        loanServelet.doPost(request, response);

        verify(loanService).createLoan(any(Loan.class));
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

  
    @Test
    void testDoPostInvalid() throws Exception {

        when(request.getReader()).thenThrow(RuntimeException.class);

        try {
            loanServelet.doPost(request, response);
        } catch (DataException e) {
         
        }
    }

  
    @Test
    void testDoGetAll() throws Exception {

        when(loanService.getAllLoans())
                .thenReturn(List.of(new Loan()));

        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        loanServelet.doGet(request, response);

        verify(loanService).getAllLoans();
    }


    @Test
    void testDoGetById() throws Exception {

        Loan loan = new Loan();
        loan.setId(1);
        loan.setLoanAccountNo("LN123");
        loan.setLoanType("HOME");
        loan.setPrincipalAmount(BigDecimal.valueOf(10000));
        loan.setStatus("ACTIVE");

        when(request.getParameter("id")).thenReturn("1");
        when(loanService.getLoanById(1L)).thenReturn(loan);

        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        loanServelet.doGet(request, response);

        verify(loanService).getLoanById(1L);
    }


    @Test
    void testDoPut() throws Exception {

        Loan loan = new Loan();
        loan.setId(1);
        loan.setLoanType("HOME");

        String json = objectMapper.writeValueAsString(loan);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));

        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        loanServelet.doPut(request, response);

        verify(loanService).updateLoan(any(Loan.class));
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }


    @Test
    void testDoPutInvalid() throws Exception {

        Loan loan = new Loan(); 

        String json = objectMapper.writeValueAsString(loan);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(json)));

        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        loanServelet.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }


    @Test
    void testDoDelete() throws Exception {

        when(request.getParameter("id")).thenReturn("1");

        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));

        loanServelet.doDelete(request, response);

        verify(loanService).deleteLoan(1L);
    }


    @Test
    void testDoDeleteInvalid() {

        when(request.getParameter("id")).thenReturn(null);

        try {
            loanServelet.doDelete(request, response);
        } catch (Exception e) {
           
        }
    }
}

