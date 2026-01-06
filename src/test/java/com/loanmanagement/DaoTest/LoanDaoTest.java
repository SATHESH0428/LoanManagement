package com.loanmanagement.DaoTest;

import com.loanmanagement.dao.CustomerDao;
import com.loanmanagement.dao.LoanDao;
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
    void testInsert() {

        Customer customer = createCustomer();
        customerDao.insert(customer);

        long customerId = fetchCustomerIdByCode("CUST001");

        Loan loan = createLoan(customerId);
        loanDao.insert(loan);

        List<Loan> loans = loanDao.getAll();
        assertEquals(1, loans.size());
        assertEquals("LN001", loans.get(0).getLoanAccountNo());
    }

    @Test
    void testGetAll() {

        customerDao.insert(createCustomer());
        long customerId = fetchCustomerIdByCode("CUST001");

        loanDao.insert(createLoan(customerId));

        List<Loan> loans = loanDao.getAll();
        assertEquals(1, loans.size());
    }

    @Test
    void testGetById() {

        customerDao.insert(createCustomer());
        long customerId = fetchCustomerIdByCode("CUST001");

        loanDao.insert(createLoan(customerId));
        Loan savedLoan = loanDao.getAll().get(0);

        Loan result = loanDao.getById(savedLoan.getId());
        assertNotNull(result);
    }

    @Test
    void testUpdate() {

        customerDao.insert(createCustomer());
        long customerId = fetchCustomerIdByCode("CUST001");

        loanDao.insert(createLoan(customerId));
        Loan loan = loanDao.getAll().get(0);

        loan.setLoanType("HOME_LOAN");
        loan.setStatus("APPROVED");

        loanDao.update(loan);

        Loan updated = loanDao.getById(loan.getId());
        assertEquals("HOME_LOAN", updated.getLoanType());
        assertEquals("APPROVED", updated.getStatus());
    }

    @Test
    void testDelete() {

        customerDao.insert(createCustomer());
        long customerId = fetchCustomerIdByCode("CUST001");

        loanDao.insert(createLoan(customerId));
        Loan loan = loanDao.getAll().get(0);

        loanDao.delete(loan.getId());

        assertNull(loanDao.getById(loan.getId()));
    }



    private long fetchCustomerIdByCode(String code) {

        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/loan_management",
                "root",
                "Admin@123");
             PreparedStatement ps =
                     con.prepareStatement("SELECT id FROM customer WHERE customer_code=?")) {

            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getLong("id");
            }
            throw new RuntimeException("Customer not found");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
