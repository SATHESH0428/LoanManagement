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

    private final LoanDao loanDao = new LoanDao();

    public void createLoan(Loan loan) {

        if (loan == null) {
            throw new IllegalArgumentException("Loan is required");
        }

        try {
            loanDao.insert(loan);
            LOG.info("Loan created successfully: {}", loan.getLoanAccountNo());
        } catch (DataException e) {
            LOG.error("Error creating loan", e);
            throw e;
        } catch (Exception e) {
            LOG.error("Error creating loan", e);
            throw new DataException("Service error while creating loan", e);
        }
    }

    public List<Loan> getAllLoans() {
        try {
            return loanDao.getAll();
        } catch (DataException e) {
            LOG.error("Error fetching all loans", e);
            throw e;
        } catch (Exception e) {
            LOG.error("Error fetching all loans", e);
            throw new DataException("Service error while fetching loans", e);
        }
    }

    public Loan getLoanById(long id) {
        try {
            return loanDao.getById(id);
        } catch (DataException e) {
            LOG.error("Error fetching loan id={}", id, e);
            throw e;
        } catch (Exception e) {
            LOG.error("Error fetching loan id={}", id, e);
            throw new DataException("Service error while fetching loan", e);
        }
    }

    public void updateLoan(Loan loan) {

        if (loan == null) {
            throw new IllegalArgumentException("Loan is required");
        }

        try {
            loanDao.update(loan);
            LOG.info("Loan updated successfully id={}", loan.getId());
        } catch (DataException e) {
            LOG.error("Error updating loan id={}", loan.getId(), e);
            throw e;
        } catch (Exception e) {
            LOG.error("Error updating loan id={}", loan.getId(), e);
            throw new DataException("Service error while updating loan", e);
        }
    }

    public void deleteLoan(long id) {
        try {
            loanDao.delete(id);
            LOG.info("Loan deleted successfully id={}", id);
        } catch (DataException e) {
            LOG.error("Error deleting loan id={}", id, e);
            throw e;
        } catch (Exception e) {
            LOG.error("Error deleting loan id={}", id, e);
            throw new DataException("Service error while deleting loan", e);
        }
    }
}
