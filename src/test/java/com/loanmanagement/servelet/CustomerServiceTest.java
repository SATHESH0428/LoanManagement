package com.loanmanagement.servelet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.loanmanagement.dao.CustomerDao;
import com.loanmanagement.model.Customer;
import com.loanmanagement.service.CustomerService;

class CustomerServiceTest {

    private CustomerService customerService;
    private CustomerDao customerDao;
    private Customer customer;

    @BeforeEach
    void setUp() throws Exception {

        customerService = new CustomerService();
        customerDao = mock(CustomerDao.class);

  
        Field field = CustomerService.class.getDeclaredField("customerDao");
        field.setAccessible(true);
        field.set(customerService, customerDao);

        customer = new Customer();
        customer.setCustomerCode("CUST001");
        customer.setName("Sathesh");
        customer.setEmail("test@mail.com");
        customer.setMobile("9876543210");
        customer.setAddress("Chennai");
    }

    @Test
    void createCustomer_success() {

        doNothing().when(customerDao).insert(any(Customer.class));

        customerService.createCustomer(customer);

        assertNotNull(customer.getCreatedDate());
        assertEquals("PENDING", customer.getKycStatus());
        verify(customerDao, times(1)).insert(customer);
    }

    @Test
    void createCustomer_customerCodeEmpty() {

        customer.setCustomerCode("");

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class,
                        () -> customerService.createCustomer(customer));

        assertEquals("Customer code is required", ex.getMessage());
        verify(customerDao, never()).insert(any());
    }

    @Test
    void createCustomer_nameNull() {

        customer.setName(null);

        IllegalArgumentException ex =
                assertThrows(IllegalArgumentException.class,
                        () -> customerService.createCustomer(customer));

        assertEquals("Customer name is required", ex.getMessage());
        verify(customerDao, never()).insert(any());
    }
}
