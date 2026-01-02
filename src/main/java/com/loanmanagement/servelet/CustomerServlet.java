package com.loanmanagement.servelet;

import java.io.IOException;
import java.io.Serial;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loanmanagement.model.Customer;
import com.loanmanagement.service.CustomerService;

@WebServlet("/customer")
public class CustomerServlet extends HttpServlet {

	private static final Logger LOG = LoggerFactory.getLogger(CustomerServlet.class);
	@Serial
    private static final long serialVersionUID = 1L;

    private final CustomerService customerService = new CustomerService();

    @Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    	LOG.info("Enter into the custemer service post method");
        try {
            Customer customer = new Customer();
            customer.setCustomerCode(request.getParameter("customerCode"));
            customer.setName(request.getParameter("name"));
            customer.setEmail(request.getParameter("email"));
            customer.setMobile(request.getParameter("mobile"));
            customer.setAddress(request.getParameter("address"));
            customer.setKycStatus(request.getParameter("kycStatus"));

            customerService.createCustomer(customer);

            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write("Customer created successfully");
            LOG.info("Customer created successfully");

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(e.getMessage());
        }
    }
    
    @Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        String id = request.getParameter("id");

        response.setContentType("application/json");

        if (id == null) {
            response.getWriter().write(
                new ObjectMapper().writeValueAsString(
                    customerService.getAllCustomers()
                )
            );
        } else {
            response.getWriter().write(
                new ObjectMapper().writeValueAsString(
                    customerService.getCustomerById(Long.parseLong(id))
                )
            );
        }
    }
    
    @Override
	public void doPut(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        Customer customer = new ObjectMapper()
                .readValue(request.getInputStream(), Customer.class);

        customerService.updateCustomer(customer);

        response.getWriter().write("Customer updated successfully");
    }

    @Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        long id = Long.parseLong(request.getParameter("id"));

        customerService.deleteCustomer(id);

        response.getWriter().write("Customer deleted successfully");
    }

}
