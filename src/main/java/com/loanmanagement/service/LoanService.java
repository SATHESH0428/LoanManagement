package com.loanmanagement.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.loanmanagement.dao.LoanDao;
import com.loanmanagement.exception.DataException;
import com.loanmanagement.model.Loan;

public class LoanService {

    private static final Logger LOG =
            LoggerFactory.getLogger(LoanService.class);

    private LoanDao loanDao = new LoanDao();

        public void createLoan(Loan loan) {
        try {
            loanDao.insert(loan);
            LOG.info("Loan created successfully: {}", loan.getLoanAccountNo());
        } catch (Exception e) {
            LOG.error("Error creating loan", e);
            throw new DataException("Service error while creating loan", e);
        }
    }

      public List<Loan> getAllLoans() {
        try {
            return loanDao.getAll();
        } catch (Exception e) {
            LOG.error("Error fetching all loans", e);
            throw new DataException("Service error while fetching loans", e);
        }
    }

     public Loan getLoanById(long id) {
        try {
            return loanDao.getById(id);
        } catch (Exception e) {
            LOG.error("Error fetching loan id={}", id, e);
            throw new DataException("Service error while fetching loan", e);
        }
    }

 
    public void updateLoan(Loan loan) {
        try {
            loanDao.update(loan);
            LOG.info("Loan updated successfully id={}", loan.getId());
        } catch (Exception e) {
            LOG.error("Error updating loan id={}", loan.getId(), e);
            throw new DataException("Service error while updating loan", e);
        }
    }

 
    public void deleteLoan(long id) {
        try {
            loanDao.delete(id);
            LOG.info("Loan deleted successfully id={}", id);
        } catch (Exception e) {
            LOG.error("Error deleting loan id={}", id, e);
            throw new DataException("Service error while deleting loan", e);
        }
    }
}
