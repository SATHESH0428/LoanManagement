package com.loanmanagement.daotest;

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

        List<Customer> customers = customerDao.findAll();
        assertEquals(1, customers.size());
        assertEquals("C001", customers.get(0).getCustomerCode());
    }

   
    @Test
    void testInsertInvalid() {

        Customer customer = new Customer();

        try {
            customerDao.insert(customer);
        } catch (DataException e) {
            assertNotNull(e.getMessage());
        }
    }

 
    @Test
    void testFindAll() {

        customerDao.insert(createCustomer());

        List<Customer> customers = customerDao.findAll();

        assertFalse(customers.isEmpty());
    }


    @Test
    void testDelete() {

        customerDao.insert(createCustomer());

        Customer customer = customerDao.findAll().get(0);
        long id = customer.getId();  

        customerDao.delete(id);

        assertEquals(0, customerDao.findAll().size());
    }

   
    @Test
    void testUpdateCustomer() {

        customerDao.insert(createCustomer());

        Customer customer = customerDao.findAll().get(0);
        customer.setName("UpdatedName");
        customer.setKycStatus("VERIFIED");

        customerDao.update(customer);

        Customer updated = customerDao.findAll().get(0);
        assertEquals("UpdatedName", updated.getName());
        assertEquals("VERIFIED", updated.getKycStatus());
    }

   

    private Customer createCustomer() {

        Customer c = new Customer();
        c.setCustomerCode("C001");
        c.setName("Sathesh");
        c.setEmail("s@gmail.com");
        c.setMobile("9999999999");
        c.setAddress("Chennai");
        c.setKycStatus("PENDING");
        c.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        return c;
    }
}
