package com.loanmanagement.daotest;

import com.loanmanagement.dao.CustomerDao;
import com.loanmanagement.dao.LoanDao;
import com.loanmanagement.exception.DataException;
import com.loanmanagement.model.Customer;
import com.loanmanagement.model.Loan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoanDaoTest {

    private LoanDao loanDao;
    private CustomerDao customerDao;

    @BeforeEach
    void setup() {

        loanDao = new LoanDao();
        customerDao = new CustomerDao();

        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/loan_management",
                "root",
                "Admin@123")) {

            Statement stmt = con.createStatement();
            stmt.execute("DELETE FROM loan");
            stmt.execute("DELETE FROM customer");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void insertLoan_success() {

        Customer customer = createCustomer();
        customerDao.insert(customer);

        Loan loan = createLoan(customer.getId());
        loanDao.insert(loan);

        List<Loan> loans = loanDao.findAll();
        assertEquals(1, loans.size());
    }

    
    @Test
    void insertLoan_invalidCustomer() {

        Loan loan = createLoan(9999L);

        assertThrows(DataException.class,
                () -> loanDao.insert(loan));
    }

 
    @Test
    void updateLoan_success() {

        Customer customer = createCustomer();
        customerDao.insert(customer);

        loanDao.insert(createLoan(customer.getId()));
        Loan loan = loanDao.findAll().get(0);

        loan.setStatus("APPROVED");

        assertDoesNotThrow(() -> loanDao.update(loan));
    }

    @Test
    void deleteLoan_invalidId() {

        assertDoesNotThrow(() -> loanDao.delete(9999L));
    }



    private Customer createCustomer() {

        Customer c = new Customer();
        c.setCustomerCode("CUST001");
        c.setName("Sathesh");
        c.setEmail("sathesh@gmail.com");
        c.setMobile("9876543210");
        c.setAddress("Chennai");
        c.setKycStatus("PENDING");
        c.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        return c;
    }

    private Loan createLoan(long customerId) {

        Loan l = new Loan();
        l.setLoanAccountNo("LN001");
        l.setCustomerId(customerId);
        l.setLoanType("PERSONAL_LOAN");
        l.setPrincipalAmount(new BigDecimal("500000"));
        l.setInterestRate(new BigDecimal("10.5"));
        l.setTenureMonths(24);
        l.setStatus("CREATED");
        l.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        return l;
    }
}
