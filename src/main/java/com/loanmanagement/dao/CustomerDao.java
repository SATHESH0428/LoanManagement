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

	static final int INSERT_CUSTOMER_CODE = 1;
	static final int INSERT_CUSTOMER_NAME = 2;
	static final int INSERT_CUSTOMER_EMAIL = 3;
	static final int INSERT_CUSTOMER_MOBILE = 4;
	static final int INSERT_CUSTOMER_ADDRESS = 5;
	static final int INSERT_CUSTOMER_KYC = 6;
	static final int INSERT_CREATED_DATE = 7;

	static final int DELETE_CUSTOMER_ID = 1;

	static final int UPDATE_CUSTOMER_KYC = 1;
	static final int UPDATE_CUSTOMER_ID = 2;

	Logger LOG = LoggerFactory.getLogger(CustomerDao.class);

	String InsertSQL = "INSERT INTO customer (customer_code,name,email,mobile,address,kyc_status,created_date) "
			+ "VALUES (?,?,?,?,?,?,?)";

	String DeleteSQL = "DELETE FROM customer WHERE id=?";

	String UpdateSQL = "UPDATE customer SET kyc_status=? WHERE id=?";

	public void insert(Customer customer) {

		try (Connection con = ConnectionClass.getConnection(); PreparedStatement ps = con.prepareStatement(InsertSQL)) {

			ps.setString(INSERT_CUSTOMER_CODE, customer.getCustomerCode());
			ps.setString(INSERT_CUSTOMER_NAME, customer.getName());
			ps.setString(INSERT_CUSTOMER_EMAIL, customer.getEmail());
			ps.setString(INSERT_CUSTOMER_MOBILE, customer.getMobile());
			ps.setString(INSERT_CUSTOMER_ADDRESS, customer.getAddress());
			ps.setString(INSERT_CUSTOMER_KYC, customer.getKycStatus());
			ps.setTimestamp(INSERT_CREATED_DATE, customer.getCreatedDate());

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

		String sql = "SELECT * FROM customer";

		try (Connection con = ConnectionClass.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
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

		String sql = "SELECT * FROM customer WHERE id=?";
		Customer customer = null;

		try (Connection con = ConnectionClass.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setLong(1, id);
			ResultSet rs = ps.executeQuery();

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

		} catch (SQLException e) {
			throw new DataException("Fetch customer failed", e);
		}

		return customer;
	}

	public void update(Customer customer) {

		String sql = "UPDATE customer SET name=?, email=?, mobile=?, address=?, kyc_status=? WHERE id=?";

		try (Connection con = ConnectionClass.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

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

		String sql = "DELETE FROM customer WHERE id=?";

		try (Connection con = ConnectionClass.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setLong(1, id);
			ps.executeUpdate();

		} catch (SQLException e) {
			throw new DataException("Delete customer failed", e);
		}
	}

}