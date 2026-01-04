package com.loanmanagement.servelet;

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

@WebServlet("/loan")
public class LoanServelet extends HttpServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final Logger LOG =
            LoggerFactory.getLogger(LoanServelet.class);

    private LoanService loanService = new LoanService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        try {
            Loan loan = mapper.readValue(req.getInputStream(), Loan.class);

            loan.setLoanAccountNo("LN" + System.currentTimeMillis());
            loan.setStatus("ACTIVE");
            loan.setCreatedDate(new Timestamp(System.currentTimeMillis()));

            loanService.createLoan(loan);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write("Loan created successfully");

        } catch (Exception e) {
            LOG.error("Loan creation failed", e);
            throw new DataException("Loan creation failed", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        try {
            String idParam = req.getParameter("id");

            if (idParam != null) {
                Loan loan = loanService.getLoanById(Long.parseLong(idParam));
                resp.getWriter().write(
                        mapper.writeValueAsString(loan)
                );
            } else {
                List<Loan> loans = loanService.getAllLoans();
                resp.getWriter().write(
                        mapper.writeValueAsString(loans)
                );
            }

        } catch (Exception e) {
            LOG.error("Loan fetch failed", e);
            throw new DataException("Loan fetch failed", e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        try {
            Loan loan = mapper.readValue(req.getInputStream(), Loan.class);

            if (loan.getId() <= 0) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Loan id is required");
                return;
            }

            loanService.updateLoan(loan);

            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("Loan updated successfully");

        } catch (Exception e) {
            LOG.error("Loan update failed", e);
            throw new DataException("Loan update failed", e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        try {
            long id = Long.parseLong(req.getParameter("id"));
            loanService.deleteLoan(id);
            resp.getWriter().write("Loan deleted successfully");

        } catch (Exception e) {
            LOG.error("Loan delete failed", e);
            throw new DataException("Loan delete failed", e);
        }
    }
}
