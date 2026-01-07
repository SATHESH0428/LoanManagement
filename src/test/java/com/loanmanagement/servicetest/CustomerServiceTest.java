package com.loanmanagement.servicetest;

import com.loanmanagement.dao.CustomerDao;
import com.loanmanagement.model.Customer;
import com.loanmanagement.service.CustomerService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    private CustomerService customerService;
    private CustomerDao customerDao;

    @BeforeEach
    void setup() throws Exception {

        customerService = new CustomerService();
        customerDao = mock(CustomerDao.class);

       
        var field = CustomerService.class.getDeclaredField("customerDao");
        field.setAccessible(true);
        field.set(customerService, customerDao);
    }

    @Test
    void testCreateCustomer() {

        Customer customer = new Customer();
        customer.setCustomerCode("C001");   
        customer.setName("Test User");      

        customerService.createCustomer(customer);

        verify(customerDao).insert(customer);
    }

    @Test
    void testGetAllCustomers() {

        List<Customer> customers = List.of(new Customer());
        when(customerDao.findAll()).thenReturn(customers);

        Object result = customerService.getAllCustomers();

        assertEquals(customers, result);
        verify(customerDao).findAll();
    }

    @Test
    void testGetCustomerById() {

        Customer customer = new Customer();
        when(customerDao.findById(1L)).thenReturn(customer);

        Object result = customerService.getCustomerById(1L);

        assertEquals(customer, result);
        verify(customerDao).findById(1L);
    }

    @Test
    void testUpdateCustomer() {

        Customer customer = new Customer();
        customer.setId(1L); 

        customerService.updateCustomer(customer);

        verify(customerDao).update(customer);
    }

    @Test
    void testDeleteCustomer() {

        customerService.deleteCustomer(1L);

        verify(customerDao).delete(1L);
    }
}
