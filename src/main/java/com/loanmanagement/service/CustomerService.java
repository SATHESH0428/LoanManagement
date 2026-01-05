package com.loanmanagement.service;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.loanmanagement.dao.CustomerDao;
import com.loanmanagement.exception.DataException;
import com.loanmanagement.model.Customer;

public class CustomerService {

    private static final Logger LOG =
            LoggerFactory.getLogger(CustomerService.class);

    private final CustomerDao customerDao = new CustomerDao();

    public void createCustomer(Customer customer) {

        if (customer == null) {
            throw new IllegalArgumentException("Customer is required");
        }

        if (customer.getCustomerCode() == null || customer.getCustomerCode().isEmpty()) {
            throw new IllegalArgumentException("Customer code is required");
        }

        if (customer.getName() == null || customer.getName().isEmpty()) {
            throw new IllegalArgumentException("Customer name is required");
        }

        if (customer.getKycStatus() == null) {
            customer.setKycStatus("PENDING");
        }

        customer.setCreatedDate(new Timestamp(System.currentTimeMillis()));

        try {
            customerDao.insert(customer);
            LOG.info("Customer created {}", customer.getCustomerCode());
        } catch (DataException e) {
            LOG.error("Customer creation failed", e);
            throw e;
        }
    }

    public List<Customer> getAllCustomers() {
        return customerDao.getAll();
    }

    public Customer getCustomerById(long id) {
        return customerDao.getById(id);
    }

    public void updateCustomer(Customer customer) {
        customerDao.update(customer);
    }

    public void deleteCustomer(long id) {
        customerDao.delete(id);
    }
}
