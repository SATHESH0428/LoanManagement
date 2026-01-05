package com.loanmanagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.loanmanagement.dbconfig.ConnectionClass;
import com.loanmanagement.exception.DataException;
import com.loanmanagement.model.Customer;

public class CustomerDao {

    private static final Logger LOG =
            LoggerFactory.getLogger(CustomerDao.class);

    private static final String INSERT_SQL =
            "INSERT INTO customer (customer_code, name, email, mobile, address, kyc_status, created_date) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_ALL_SQL =
            "SELECT * FROM customer";

    private static final String SELECT_BY_ID_SQL =
            "SELECT * FROM customer WHERE id=?";

    private static final String UPDATE_SQL =
            "UPDATE customer SET name=?, email=?, mobile=?, address=?, kyc_status=? WHERE id=?";

    private static final String DELETE_SQL =
            "DELETE FROM customer WHERE id=?";

    public void insert(Customer customer) {

        try (Connection con = ConnectionClass.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_SQL)) {

            ps.setString(1, customer.getCustomerCode());
            ps.setString(2, customer.getName());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getMobile());
            ps.setString(5, customer.getAddress());
            ps.setString(6, customer.getKycStatus());
            ps.setTimestamp(7, customer.getCreatedDate());

            int rows = ps.executeUpdate();
            if (rows == 0) {
                LOG.error("Insert failed for Customer {}", customer.getCustomerCode());
            } else {
                LOG.info("Customer inserted {}", customer.getCustomerCode());
            }

        } catch (SQLException e) {
            throw new DataException("Failed to insert Customer", e);
        }
    }

    public List<Customer> getAll() {

        List<Customer> customers = new ArrayList<>();

        try (Connection con = ConnectionClass.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Customer c = new Customer();
                c.setId(rs.getLong("id"));
                c.setCustomerCode(rs.getString("customer_code"));
                c.setName(rs.getString("name"));
                c.setEmail(rs.getString("email"));
                c.setMobile(rs.getString("mobile"));
                c.setAddress(rs.getString("address"));
                c.setKycStatus(rs.getString("kyc_status"));
                c.setCreatedDate(rs.getTimestamp("created_date"));
                customers.add(c);
            }

        } catch (SQLException e) {
            throw new DataException("Fetch all customers failed", e);
        }

        return customers;
    }

    public Customer getById(long id) {

        Customer customer = null;

        try (Connection con = ConnectionClass.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_ID_SQL)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    customer = new Customer();
                    customer.setId(rs.getLong("id"));
                    customer.setCustomerCode(rs.getString("customer_code"));
                    customer.setName(rs.getString("name"));
                    customer.setEmail(rs.getString("email"));
                    customer.setMobile(rs.getString("mobile"));
                    customer.setAddress(rs.getString("address"));
                    customer.setKycStatus(rs.getString("kyc_status"));
                    customer.setCreatedDate(rs.getTimestamp("created_date"));
                }
            }

        } catch (SQLException e) {
            throw new DataException("Fetch customer failed", e);
        }

        return customer;
    }

    public void update(Customer customer) {

        try (Connection con = ConnectionClass.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, customer.getName());
            ps.setString(2, customer.getEmail());
            ps.setString(3, customer.getMobile());
            ps.setString(4, customer.getAddress());
            ps.setString(5, customer.getKycStatus());
            ps.setLong(6, customer.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DataException("Update customer failed", e);
        }
    }

    public void delete(long id) {

        try (Connection con = ConnectionClass.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE_SQL)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DataException("Delete customer failed", e);
        }
    }
}
