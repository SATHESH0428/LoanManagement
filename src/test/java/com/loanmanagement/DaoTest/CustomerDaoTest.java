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
            stmt.execute("DELETE FROM loan");     
            stmt.execute("DELETE FROM customer");
        }
    }

    @Test
    void testInsert() {

        Customer customer = createCustomer();

        customerDao.insert(customer);

        assertTrue(customer.getId() > 0);

        Customer saved = customerDao.getById(customer.getId());
        assertNotNull(saved);
        assertEquals("CUST001", saved.getCustomerCode());
    }

    @Test
    void testInsertInvalid() {

        Customer customer = new Customer(); 

        assertThrows(DataException.class, () -> customerDao.insert(customer));
    }

    @Test
    void testGetAll() {

        customerDao.insert(createCustomer());

        List<Customer> customers = customerDao.getAll();

        assertEquals(1, customers.size());
    }

    @Test
    void testGetById() {

        Customer customer = createCustomer();
        customerDao.insert(customer);

        Customer result = customerDao.getById(customer.getId());

        assertNotNull(result);
        assertEquals(customer.getCustomerCode(), result.getCustomerCode());
    }

    @Test
    void testUpdate() {

        Customer customer = createCustomer();
        customerDao.insert(customer);

        customer.setName("Updated Name");
        customer.setKycStatus("VERIFIED");

        customerDao.update(customer);

        Customer updated = customerDao.getById(customer.getId());
        assertEquals("Updated Name", updated.getName());
        assertEquals("VERIFIED", updated.getKycStatus());
    }

    @Test
    void testDelete() {

        Customer customer = createCustomer();
        customerDao.insert(customer);

        customerDao.delete(customer.getId());

        Customer deleted = customerDao.getById(customer.getId());
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
}

