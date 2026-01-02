package com.loanmanagement.servelet;

import java.io.IOException;
import java.io.Serial;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.ServletException;
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

      @Override
      protected void doPost(HttpServletRequest req, HttpServletResponse resp)
              throws IOException {

          try {
             
              StringBuilder json = new StringBuilder();
              String line;
              while ((line = req.getReader().readLine()) != null) {
                  json.append(line);
              }

              ObjectMapper mapper = new ObjectMapper();
              Loan loan = mapper.readValue(json.toString(), Loan.class);

              
              loan.setLoanAccountNo("LN" + System.currentTimeMillis());
              loan.setStatus("ACTIVE");
              loan.setCreatedDate(new Timestamp(System.currentTimeMillis()));

              loanService.createLoan(loan);

              resp.setStatus(HttpServletResponse.SC_CREATED);
              resp.getWriter().println("Loan created successfully");

          } catch (Exception e) {
              LOG.error("Loan creation failed", e);
              throw new DataException("Loan creation failed", e);
          }
      }


     @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            String idParam = req.getParameter("id");

            if (idParam != null) {
                long id = Long.parseLong(idParam);
                Loan loan = loanService.getLoanById(id);

                if (loan == null) {
                    resp.getWriter().println("Loan not found");
                    return;
                }

                resp.getWriter().println("Loan Account No: " + loan.getLoanAccountNo());
                resp.getWriter().println("Loan Type: " + loan.getLoanType());
                resp.getWriter().println("Amount: " + loan.getPrincipalAmount());
                resp.getWriter().println("Status: " + loan.getStatus());
            } else {
                List<Loan> loans = loanService.getAllLoans();
                for (Loan loan : loans) {
                    resp.getWriter().println(
                        loan.getId() + " | " +
                        loan.getLoanAccountNo() + " | " +
                        loan.getLoanType() + " | " +
                        loan.getStatus()
                    );
                }
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
            
             StringBuilder json = new StringBuilder();
             String line;
             while ((line = req.getReader().readLine()) != null) {
                 json.append(line);
             }


             ObjectMapper mapper = new ObjectMapper();
             Loan loan = mapper.readValue(json.toString(), Loan.class);

            
             if (loan.getId() <= 0) {
                 resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                 resp.getWriter().println("Loan id is required for update");
                 return;
             }

             loanService.updateLoan(loan);

             resp.setStatus(HttpServletResponse.SC_OK);
             resp.getWriter().println("Loan updated successfully");

         } catch (Exception e) {
             LOG.error("Loan update failed", e);
             throw new DataException("Loan update failed", e);
         }
     }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            long id = Long.parseLong(req.getParameter("id"));
            loanService.deleteLoan(id);

            resp.getWriter().println("Loan deleted successfully");

        } catch (Exception e) {
            LOG.error("Loan delete failed", e);
            throw new DataException("Loan delete failed", e);
        }
    }
}
