package com.loanmanagement.DaoTest;

import com.loanmanagement.dao.CustomerDao;
import com.loanmanagement.exception.DataException;
import com.loanmanagement.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;

class CustomerDaoTest {

    private CustomerDao customerDao;

    @BeforeEach
    void setup() {

        customerDao = new CustomerDao();

        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/loan_management",
                "root",
                "Admin@123")) {

            Statement stmt = con.createStatement();
            stmt.execute("DELETE FROM loan");
            stmt.execute("DELETE FROM customer");

        } catch (Exception e) {
            throw new RuntimeException("Test DB setup failed", e);
        }
    }

    @Test
    void testInsert() {
        Customer customer = createCustomer();
        assertDoesNotThrow(() -> customerDao.insert(customer));
    }

    @Test
    void testInsertInvalid() {
        Customer customer = new Customer();
        assertThrows(DataException.class, () -> customerDao.insert(customer));
    }

    @Test
    void testGetAll() {
        customerDao.insert(createCustomer());
        assertDoesNotThrow(() -> customerDao.getAll());
    }

    @Test
    void testGetById() {
        Customer customer = createCustomer();
        customerDao.insert(customer);
        assertDoesNotThrow(() -> customerDao.getById(customer.getId()));
    }

    @Test
    void testUpdate() {
        Customer customer = createCustomer();
        customerDao.insert(customer);

        customer.setName("Updated Name");
        customer.setKycStatus("VERIFIED");

        assertDoesNotThrow(() -> customerDao.update(customer));
    }

    @Test
    void testDelete() {
        Customer customer = createCustomer();
        customerDao.insert(customer);
        assertDoesNotThrow(() -> customerDao.delete(customer.getId()));
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
}
