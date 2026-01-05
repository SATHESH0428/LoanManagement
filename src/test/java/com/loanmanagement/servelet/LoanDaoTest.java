package com.loanmanagement.servelet;






import com.loanmanagement.dao.CustomerDao;
import com.loanmanagement.dao.LoanDao;
import com.loanmanagement.model.Customer;
import com.loanmanagement.model.Loan;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoanDaoTest {

    private LoanDao loanDao;
    private CustomerDao customerDao;

    @BeforeEach
    void setup() throws Exception {

        loanDao = new LoanDao();
        customerDao = new CustomerDao();

        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/loan_management",
                "root",
                "Admin@123")) {

            Statement stmt = con.createStatement();
            stmt.execute("DELETE FROM loan");     
            stmt.execute("DELETE FROM customer"); 
        }
    }

    @Test
    void testInsert() {

        Customer customer = createCustomer();
        customerDao.insert(customer);

        assertTrue(customer.getId() > 0);

        Loan loan = createLoan(customer.getId());
        loanDao.insert(loan);

        assertTrue(loan.getId() > 0);

        Loan saved = loanDao.getById(loan.getId());
        assertNotNull(saved);
        assertEquals("LN001", saved.getLoanAccountNo());
    }

    @Test
    void testGetAll() {

        Customer customer = createCustomer();
        customerDao.insert(customer);

        loanDao.insert(createLoan(customer.getId()));

        List<Loan> loans = loanDao.getAll();
        assertEquals(1, loans.size());
    }

    @Test
    void testGetById() {

        Customer customer = createCustomer();
        customerDao.insert(customer);

        Loan loan = createLoan(customer.getId());
        loanDao.insert(loan);

        Loan result = loanDao.getById(loan.getId());
        assertNotNull(result);
        assertEquals(loan.getLoanAccountNo(), result.getLoanAccountNo());
    }

    @Test
    void testUpdate() {

        Customer customer = createCustomer();
        customerDao.insert(customer);

        Loan loan = createLoan(customer.getId());
        loanDao.insert(loan);

        loan.setLoanType("HOME_LOAN");
        loan.setStatus("APPROVED");

        loanDao.update(loan);

        Loan updated = loanDao.getById(loan.getId());
        assertNotNull(updated);
        assertEquals("HOME_LOAN", updated.getLoanType());
        assertEquals("APPROVED", updated.getStatus());
    }

    @Test
    void testDelete() {

        Customer customer = createCustomer();
        customerDao.insert(customer);

        Loan loan = createLoan(customer.getId());
        loanDao.insert(loan);

        loanDao.delete(loan.getId());

        Loan deleted = loanDao.getById(loan.getId());
        assertNull(deleted);
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
