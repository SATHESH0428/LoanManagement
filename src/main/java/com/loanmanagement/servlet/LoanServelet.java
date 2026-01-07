package com.loanmanagement.servlet;

import java.io.IOException;
import java.io.Serial;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loanmanagement.exception.DataException;
import com.loanmanagement.model.Loan;
import com.loanmanagement.service.LoanService;


public class LoanServelet extends HttpServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    static final Logger LOG =
            LoggerFactory.getLogger(LoanServelet.class);

    private static final String JSON_TYPE = "application/json";
    private static final String INVALID_LOAN_ID = "Invalid loan id";
    private static final String LOAN_CREATED = "Loan created successfully";
    private static final String LOAN_UPDATED = "Loan updated successfully";
    private static final String LOAN_DELETED = "Loan deleted successfully";
    private static final String LOAN_ID_REQUIRED = "Loan id is required";

    private static  LoanService loanService = new LoanService();
    private static  ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType(JSON_TYPE);

        try {
            Loan loan = mapper.readValue(req.getInputStream(), Loan.class);

            loan.setLoanAccountNo("LN" + System.currentTimeMillis());
            loan.setStatus("ACTIVE");
            loan.setCreatedDate(new Timestamp(System.currentTimeMillis()));

            loanService.createLoan(loan);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(LOAN_CREATED);

        } catch (IOException e) {
            
            throw new DataException("Invalid loan request payload", e);
        } catch (Exception e) {
            
            throw new DataException("Loan creation failed", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType(JSON_TYPE);

        try {
            String idParam = req.getParameter("id");

            if (idParam != null && !idParam.isBlank()) {
                long id = Long.parseLong(idParam);
                Loan loan = loanService.getLoanById(id);
                resp.getWriter().write(mapper.writeValueAsString(loan));
            } else {
                List<Loan> loans = loanService.getAllLoans();
                resp.getWriter().write(mapper.writeValueAsString(loans));
            }

        } catch (NumberFormatException e) {
           
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(INVALID_LOAN_ID);
        } catch (Exception e) {
          
            throw new DataException("Loan fetch failed", e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType(JSON_TYPE);

        try {
            Loan loan = mapper.readValue(req.getInputStream(), Loan.class);

            if (loan.getId() <= 0) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(LOAN_ID_REQUIRED);
                return;
            }

            loanService.updateLoan(loan);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(LOAN_UPDATED);

        } catch (IOException e) {
            
            throw new DataException("Invalid loan update payload", e);
        } catch (Exception e) {
            
            throw new DataException("Loan update failed", e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType(JSON_TYPE);

        try {
            long id = Long.parseLong(req.getParameter("id"));
            loanService.deleteLoan(id);
            resp.getWriter().write(LOAN_DELETED);

        } catch (NumberFormatException e) {
            
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(INVALID_LOAN_ID);
        } catch (Exception e) {
            
            throw new DataException("Loan delete failed", e);
        }
    }
}
