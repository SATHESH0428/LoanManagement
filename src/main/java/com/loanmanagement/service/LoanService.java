package com.loanmanagement.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.loanmanagement.dao.LoanDao;
import com.loanmanagement.exception.DataException;
import com.loanmanagement.model.Loan;

public class LoanService {

    static final Logger LOG =
            LoggerFactory.getLogger(LoanService.class);

    private final LoanDao loanDao = new LoanDao();

    public void createLoan(Loan loan) {

        if (loan == null) {
            throw new IllegalArgumentException("Loan is required");
        }

        try {
            loanDao.insert(loan);
            LOG.info("Loan created successfully: {}", loan.getLoanAccountNo());
        } catch (DataException e) {
            
            throw e;
        } catch (Exception e) {
           
            throw new DataException("Service error while creating loan", e);
        }
    }

    public List<Loan> getAllLoans() {
        try {
            return loanDao.findAll();
        } catch (DataException e) {
         
            throw e;
        } catch (Exception e) {
        
            throw new DataException("Service error while fetching loans", e);
        }
    }

    public Loan getLoanById(long id) {
        try {
            return loanDao.findById(id);
        } catch (DataException e) {
            
            throw e;
        } catch (Exception e) {
            
            throw new DataException("Service error while fetching loan", e);
        }
    }

    public void updateLoan(Loan loan) {

        if (loan == null) {
            throw new IllegalArgumentException("Loan is required");
        }

        try {
            loanDao.update(loan);
           
        } catch (DataException e) {
           
            throw e;
        } catch (Exception e) {
            
            throw new DataException("Service error while updating loan", e);
        }
    }

    public void deleteLoan(long id) {
        try {
            loanDao.delete(id);
            LOG.info("Loan deleted successfully id={}", id);
        } catch (DataException e) {
            
            throw e;
        } catch (Exception e) {
          
            throw new DataException("Service error while deleting loan", e);
        }
    }
}
