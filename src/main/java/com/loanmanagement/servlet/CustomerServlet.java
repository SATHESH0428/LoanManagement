package com.loanmanagement.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serial;

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

    private static final String APPLICATION_JSON = "application/json";
    private static final String INVALID_CUSTOMER_ID = "Invalid customer id";
    private static final String CUSTOMER_CREATED = "Customer created successfully";
    private static final String CUSTOMER_UPDATED = "Customer updated successfully";
    private static final String CUSTOMER_DELETED = "Customer deleted successfully";
    private static final String INTERNAL_ERROR = "Internal server error";

    private static final CustomerService customerService = new CustomerService();
    private static final ObjectMapper mapper = new ObjectMapper();


    private void writeJson(HttpServletResponse resp, Object data) {
        try {
            resp.getWriter().write(mapper.writeValueAsString(data));
        } catch (IOException e) {
            LOG.error("Response write failed", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            LOG.error("JSON serialization failed", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType(APPLICATION_JSON);

        try (PrintWriter out = resp.getWriter()) {

            Customer customer = new Customer();
            customer.setCustomerCode(req.getParameter("customerCode"));
            customer.setName(req.getParameter("name"));
            customer.setEmail(req.getParameter("email"));
            customer.setMobile(req.getParameter("mobile"));
            customer.setAddress(req.getParameter("address"));
            customer.setKycStatus(req.getParameter("kycStatus"));

            customerService.createCustomer(customer);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            out.write(CUSTOMER_CREATED);

        } catch (Exception e) {
            LOG.error("Customer creation failed", e);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType(APPLICATION_JSON);

        String id = req.getParameter("id");

        try {
            if (id == null || id.isBlank()) {
                writeJson(resp, customerService.getAllCustomers());
            } else {
                long customerId = Long.parseLong(id);
                writeJson(resp, customerService.getCustomerById(customerId));
            }
        } catch (NumberFormatException e) {
            LOG.error(INVALID_CUSTOMER_ID, e);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(INVALID_CUSTOMER_ID);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType(APPLICATION_JSON);

        try {
            Customer customer =
                    mapper.readValue(req.getInputStream(), Customer.class);

            customerService.updateCustomer(customer);

            resp.getWriter().write(CUSTOMER_UPDATED);

        } catch (Exception e) {
            LOG.error("Customer update failed", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        resp.setContentType(APPLICATION_JSON);

        try {
            long id = Long.parseLong(req.getParameter("id"));
            customerService.deleteCustomer(id);
            resp.getWriter().write(CUSTOMER_DELETED);

        } catch (NumberFormatException e) {
            LOG.error(INVALID_CUSTOMER_ID, e);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(INVALID_CUSTOMER_ID);
        } catch (Exception e) {
            LOG.error("Customer delete failed", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
