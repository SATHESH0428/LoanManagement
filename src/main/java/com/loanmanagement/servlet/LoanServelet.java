package com.loanmanagement.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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

    private final LoanService loanService = new LoanService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try {
            Loan loan = mapper.readValue(req.getInputStream(), Loan.class);

            loan.setLoanAccountNo("LN" + System.currentTimeMillis());
            loan.setStatus("ACTIVE");
            loan.setCreatedDate(new Timestamp(System.currentTimeMillis()));

            loanService.createLoan(loan);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            out.write("Loan created successfully");

        } catch (IOException e) {
            LOG.error("Invalid loan request payload", e);
            throw new DataException("Invalid loan request payload", e);
        } catch (Exception e) {
            LOG.error("Loan creation failed", e);
            throw new DataException("Loan creation failed", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try {
            String idParam = req.getParameter("id");

            if (idParam != null && !idParam.isBlank()) {
                long id = Long.parseLong(idParam);
                Loan loan = loanService.getLoanById(id);
                out.write(mapper.writeValueAsString(loan));
            } else {
                List<Loan> loans = loanService.getAllLoans();
                out.write(mapper.writeValueAsString(loans));
            }

        } catch (NumberFormatException e) {
            LOG.error("Invalid loan id", e);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("Invalid loan id");
        } catch (Exception e) {
            LOG.error("Loan fetch failed", e);
            throw new DataException("Loan fetch failed", e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try {
            Loan loan = mapper.readValue(req.getInputStream(), Loan.class);

            if (loan.getId() <= 0) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                out.write("Loan id is required");
                return;
            }

            loanService.updateLoan(loan);

            resp.setStatus(HttpServletResponse.SC_OK);
            out.write("Loan updated successfully");

        } catch (IOException e) {
            LOG.error("Invalid loan update payload", e);
            throw new DataException("Invalid loan update payload", e);
        } catch (Exception e) {
            LOG.error("Loan update failed", e);
            throw new DataException("Loan update failed", e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try {
            String idParam = req.getParameter("id");
            long id = Long.parseLong(idParam);

            loanService.deleteLoan(id);
            out.write("Loan deleted successfully");

        } catch (NumberFormatException e) {
            LOG.error("Invalid loan id", e);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("Invalid loan id");
        } catch (Exception e) {
            LOG.error("Loan delete failed", e);
            throw new DataException("Loan delete failed", e);
        }
    }
}
