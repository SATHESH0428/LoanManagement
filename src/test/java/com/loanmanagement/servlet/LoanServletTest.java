package com.loanmanagement.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loanmanagement.model.Loan;
import com.loanmanagement.service.LoanService;
import com.loanmanagement.servlet.LoanServelet;

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

class LoanServeletTest {

	private LoanServelet loanServelet;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private LoanService loanService;

	private final ObjectMapper mapper = new ObjectMapper();

	@BeforeEach
	void setUp() throws Exception {

		loanServelet = new LoanServelet();
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		loanService = mock(LoanService.class);

		var field = LoanServelet.class.getDeclaredField("loanService");
		field.setAccessible(true);
		field.set(loanServelet, loanService);

		when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
	}

	@Test
	void testDoPost() throws Exception {

		Loan loan = new Loan();
		loan.setLoanType("PERSONAL");

		String json = mapper.writeValueAsString(loan);
		when(request.getInputStream()).thenReturn(inputStream(json));

		loanServelet.doPost(request, response);

		verify(loanService).createLoan(any(Loan.class));
		verify(response).setStatus(HttpServletResponse.SC_CREATED);
	}

	@Test
	void testDoGetAll() throws Exception {

		loanServelet.doGet(request, response);

		verify(loanService).getAllLoans();
	}

	@Test
	void testDoGetById() throws Exception {

		when(request.getParameter("id")).thenReturn("1");

		loanServelet.doGet(request, response);

		verify(loanService).getLoanById(1L);
	}

	@Test
	void testDoPut() throws Exception {

		Loan loan = new Loan();
		loan.setId(1);
		loan.setLoanType("HOME");

		String json = mapper.writeValueAsString(loan);
		when(request.getInputStream()).thenReturn(inputStream(json));

		loanServelet.doPut(request, response);

		verify(loanService).updateLoan(any(Loan.class));
	}

	@Test
	void testDoDelete() throws Exception {

		when(request.getParameter("id")).thenReturn("1");

		loanServelet.doDelete(request, response);

		verify(loanService).deleteLoan(1L);
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
			public void setReadListener(ReadListener readListener) {
			}

			@Override
			public int read() throws IOException {
				return bis.read();
			}
		};
	}
}
