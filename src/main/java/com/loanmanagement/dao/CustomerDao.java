package com.loanmanagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.loanmanagement.dbconfig.ConnectionClass;
import com.loanmanagement.exception.DataException;
import com.loanmanagement.model.Customer;

public class CustomerDao {

	static final Logger LOG = LoggerFactory.getLogger(CustomerDao.class);

	private static final int INSERT_CUSTOMER_CODE = 1;
	private static final int INSERT_NAME = 2;
	private static final int INSERT_EMAIL = 3;
	private static final int INSERT_MOBILE = 4;
	private static final int INSERT_ADDRESS = 5;
	private static final int INSERT_KYC = 6;
	private static final int INSERT_CREATED_DATE = 7;

	private static final int SELECT_BY_ID = 1;
	private static final int DELETE_BY_ID = 1;

	private static final int UPDATE_NAME = 1;
	private static final int UPDATE_EMAIL = 2;
	private static final int UPDATE_MOBILE = 3;
	private static final int UPDATE_ADDRESS = 4;
	private static final int UPDATE_KYC = 5;
	private static final int UPDATE_ID = 6;

	private static final String INSERT_SQL = "INSERT INTO customer (customer_code, name, email, mobile, address, kyc_status, created_date) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?)";

	private static final String SELECT_ALL_SQL = "SELECT * FROM customer";

	private static final String SELECT_BY_ID_SQL = "SELECT * FROM customer WHERE id=?";

	private static final String UPDATE_SQL = "UPDATE customer SET name=?, email=?, mobile=?, address=?, kyc_status=? WHERE id=?";

	private static final String DELETE_SQL = "DELETE FROM customer WHERE id=?";

	public void insert(Customer customer) {

		try (Connection con = ConnectionClass.getConnection();
				PreparedStatement ps = con.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(INSERT_CUSTOMER_CODE, customer.getCustomerCode());
			ps.setString(INSERT_NAME, customer.getName());
			ps.setString(INSERT_EMAIL, customer.getEmail());
			ps.setString(INSERT_MOBILE, customer.getMobile());
			ps.setString(INSERT_ADDRESS, customer.getAddress());
			ps.setString(INSERT_KYC, customer.getKycStatus());
			ps.setTimestamp(INSERT_CREATED_DATE, customer.getCreatedDate());

			int rows = ps.executeUpdate();

			if (rows == 0) {
				LOG.error("Insert failed for customer {}", customer.getCustomerCode());
			} else {

				try (ResultSet rs = ps.getGeneratedKeys()) {
					if (rs.next()) {
						customer.setId(rs.getLong(1));
					}
				}
				LOG.info("Customer inserted successfully {}", customer.getCustomerCode());
			}

		} catch (SQLException e) {
			throw new DataException("Failed to insert customer", e);
		}
	}

	public List<Customer> findAll() {

		List<Customer> customers = new ArrayList<>();

		try (Connection con = ConnectionClass.getConnection();
				PreparedStatement ps = con.prepareStatement(SELECT_ALL_SQL);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				customers.add(mapping(rs));
			}

			LOG.info("Fetched all customers, count={}", customers.size());
			return customers;

		} catch (SQLException e) {
			throw new DataException("Failed to fetch customers", e);
		}
	}

	public Customer findById(long id) {

		try (Connection con = ConnectionClass.getConnection();
				PreparedStatement ps = con.prepareStatement(SELECT_BY_ID_SQL)) {

			ps.setLong(SELECT_BY_ID, id);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mapping(rs);
				}
			}

			LOG.warn("Customer not found with id={}", id);
			return null;

		} catch (SQLException e) {
			throw new DataException("Failed to fetch customer by id", e);
		}
	}

	public void update(Customer customer) {

		try (Connection con = ConnectionClass.getConnection();
				PreparedStatement ps = con.prepareStatement(UPDATE_SQL)) {

			ps.setString(UPDATE_NAME, customer.getName());
			ps.setString(UPDATE_EMAIL, customer.getEmail());
			ps.setString(UPDATE_MOBILE, customer.getMobile());
			ps.setString(UPDATE_ADDRESS, customer.getAddress());
			ps.setString(UPDATE_KYC, customer.getKycStatus());
			ps.setLong(UPDATE_ID, customer.getId());

			int rows = ps.executeUpdate();
			if (rows == 0) {
				LOG.error("Update failed for customer id={}", customer.getId());
			} else {
				LOG.info("Customer updated successfully id={}", customer.getId());
			}

		} catch (SQLException e) {
			throw new DataException("Failed to update customer", e);
		}
	}

	public void delete(long id) {

		try (Connection con = ConnectionClass.getConnection();
				PreparedStatement ps = con.prepareStatement(DELETE_SQL)) {

			ps.setLong(DELETE_BY_ID, id);

			int rows = ps.executeUpdate();
			if (rows == 0) {
				LOG.error("Delete failed, customer not found id={}", id);
			} else {
				LOG.info("Customer deleted successfully id={}", id);
			}

		} catch (SQLException e) {
			throw new DataException("Failed to delete customer", e);
		}
	}

	private static Customer mapping(ResultSet rs) throws SQLException {

		Customer customer = new Customer();
		customer.setId(rs.getLong("id"));
		customer.setCustomerCode(rs.getString("customer_code"));
		customer.setName(rs.getString("name"));
		customer.setEmail(rs.getString("email"));
		customer.setMobile(rs.getString("mobile"));
		customer.setAddress(rs.getString("address"));
		customer.setKycStatus(rs.getString("kyc_status"));
		customer.setCreatedDate(rs.getTimestamp("created_date"));
		return customer;
	}
}
