package com.loanmanagement.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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

    @Serial
    private static final long serialVersionUID = 1L;

    private static final Logger LOG =
            LoggerFactory.getLogger(CustomerServlet.class);

    private final CustomerService customerService = new CustomerService();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        LOG.info("Entering customer POST");

        try {
            Customer customer = new Customer();
            customer.setCustomerCode(req.getParameter("customerCode"));
            customer.setName(req.getParameter("name"));
            customer.setEmail(req.getParameter("email"));
            customer.setMobile(req.getParameter("mobile"));
            customer.setAddress(req.getParameter("address"));
            customer.setKycStatus(req.getParameter("kycStatus"));

            customerService.createCustomer(customer);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            out.write("Customer created successfully");

            LOG.info("Customer created successfully");

        } catch (Exception e) {
            LOG.error("Customer creation failed", e);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write(e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try {
            String id = req.getParameter("id");

            if (id == null || id.isBlank()) {
                out.write(
                        mapper.writeValueAsString(
                                customerService.getAllCustomers()
                        )
                );
            } else {
                long customerId = Long.parseLong(id);
                out.write(
                        mapper.writeValueAsString(
                                customerService.getCustomerById(customerId)
                        )
                );
            }

        } catch (NumberFormatException e) {
            LOG.error("Invalid customer id", e);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("Invalid customer id");
        } catch (Exception e) {
            LOG.error("Customer fetch failed", e);
            throw e;
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try {
            Customer customer =
                    mapper.readValue(req.getInputStream(), Customer.class);

            customerService.updateCustomer(customer);

            out.write("Customer updated successfully");

        } catch (Exception e) {
            LOG.error("Customer update failed", e);
            throw e;
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try {
            long id = Long.parseLong(req.getParameter("id"));
            customerService.deleteCustomer(id);
            out.write("Customer deleted successfully");

        } catch (NumberFormatException e) {
            LOG.error("Invalid customer id", e);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            out.write("Invalid customer id");
        }
    }
}
