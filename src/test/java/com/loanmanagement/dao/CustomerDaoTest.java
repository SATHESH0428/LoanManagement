package com.loanmanagement.dao;





import com.loanmanagement.dao.CustomerDao;
import com.loanmanagement.exception.DataException;
import com.loanmanagement.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerDaoTest {

    private CustomerDao customerDao;

    @BeforeEach
    void setup() throws Exception {

        customerDao = new CustomerDao();

        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/loan_management",
                "root",
                "Admin@123")) {

            Statement stmt = con.createStatement();
            stmt.execute("DELETE FROM loan");     // FK dependency
            stmt.execute("DELETE FROM customer");
        }
    }

    @Test
    void testInsert() {

        Customer customer = createCustomer();

        customerDao.insert(customer);

        List<Customer> customers = customerDao.getAll();

        assertEquals(1, customers.size());
        assertEquals("CUST001", customers.get(0).getCustomerCode());
    }

    @Test
    void testInsertInvalid() {

        Customer customer = new Customer(); // empty object

        assertThrows(DataException.class, () -> customerDao.insert(customer));
    }

    @Test
    void testGetAll() {

        customerDao.insert(createCustomer());

        List<Customer> customers = customerDao.getAll();

        assertFalse(customers.isEmpty());
    }

    @Test
    void testGetById() {

        customerDao.insert(createCustomer());

        Customer customer = customerDao.getById(1);

        assertNotNull(customer);
        assertEquals("CUST001", customer.getCustomerCode());
    }

    @Test
    void testUpdate() {

        customerDao.insert(createCustomer());

        Customer updated = customerDao.getById(1);
        updated.setName("Updated Name");
        updated.setKycStatus("VERIFIED");

        customerDao.update(updated);

        Customer result = customerDao.getById(1);
        assertEquals("Updated Name", result.getName());
        assertEquals("VERIFIED", result.getKycStatus());
    }

    @Test
    void testDelete() {

        customerDao.insert(createCustomer());

        customerDao.delete(1);

        assertEquals(0, customerDao.getAll().size());
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
