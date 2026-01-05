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
import com.loanmanagement.model.Loan;

public class LoanDao {

    private static final Logger LOG =
            LoggerFactory.getLogger(LoanDao.class);

    private static final String INSERT_SQL =
            "INSERT INTO loan (loan_account_no, customer_id, loan_type, principal_amount, interest_rate, tenure_months, status, created_date) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_ALL_SQL =
            "SELECT * FROM loan";

    private static final String SELECT_BY_ID_SQL =
            "SELECT * FROM loan WHERE id=?";

    private static final String UPDATE_SQL =
            "UPDATE loan SET loan_type=?, principal_amount=?, interest_rate=?, tenure_months=?, status=? WHERE id=?";

    private static final String DELETE_SQL =
            "DELETE FROM loan WHERE id=?";

    public void insert(Loan loan) {

        try (Connection con = ConnectionClass.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_SQL)) {

            ps.setString(1, loan.getLoanAccountNo());
            ps.setLong(2, loan.getCustomerId());
            ps.setString(3, loan.getLoanType());
            ps.setBigDecimal(4, loan.getPrincipalAmount());
            ps.setBigDecimal(5, loan.getInterestRate());
            ps.setInt(6, loan.getTenureMonths());
            ps.setString(7, loan.getStatus());
            ps.setTimestamp(8, loan.getCreatedDate());

            int rows = ps.executeUpdate();
            if (rows == 0) {
                LOG.error("Insert failed for Loan {}", loan.getLoanAccountNo());
            } else {
                LOG.info("Loan inserted {}", loan.getLoanAccountNo());
            }

        } catch (SQLException e) {
            throw new DataException("Failed to insert Loan", e);
        }
    }

    public List<Loan> getAll() {

        List<Loan> loans = new ArrayList<>();

        try (Connection con = ConnectionClass.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Loan loan = new Loan();
                loan.setId(rs.getLong("id"));
                loan.setLoanAccountNo(rs.getString("loan_account_no"));
                loan.setCustomerId(rs.getLong("customer_id"));
                loan.setLoanType(rs.getString("loan_type"));
                loan.setPrincipalAmount(rs.getBigDecimal("principal_amount"));
                loan.setInterestRate(rs.getBigDecimal("interest_rate"));
                loan.setTenureMonths(rs.getInt("tenure_months"));
                loan.setStatus(rs.getString("status"));
                loan.setCreatedDate(rs.getTimestamp("created_date"));
                loans.add(loan);
            }

        } catch (SQLException e) {
            throw new DataException("Fetch all loans failed", e);
        }

        return loans;
    }

    public Loan getById(long id) {

        Loan loan = null;

        try (Connection con = ConnectionClass.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_ID_SQL)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    loan = new Loan();
                    loan.setId(rs.getLong("id"));
                    loan.setLoanAccountNo(rs.getString("loan_account_no"));
                    loan.setCustomerId(rs.getLong("customer_id"));
                    loan.setLoanType(rs.getString("loan_type"));
                    loan.setPrincipalAmount(rs.getBigDecimal("principal_amount"));
                    loan.setInterestRate(rs.getBigDecimal("interest_rate"));
                    loan.setTenureMonths(rs.getInt("tenure_months"));
                    loan.setStatus(rs.getString("status"));
                    loan.setCreatedDate(rs.getTimestamp("created_date"));
                }
            }

        } catch (SQLException e) {
            throw new DataException("Fetch loan failed", e);
        }

        return loan;
    }

    public void update(Loan loan) {

        try (Connection con = ConnectionClass.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_SQL)) {

            ps.setString(1, loan.getLoanType());
            ps.setBigDecimal(2, loan.getPrincipalAmount());
            ps.setBigDecimal(3, loan.getInterestRate());
            ps.setInt(4, loan.getTenureMonths());
            ps.setString(5, loan.getStatus());
            ps.setLong(6, loan.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DataException("Update loan failed", e);
        }
    }

    public void delete(long id) {

        try (Connection con = ConnectionClass.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE_SQL)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DataException("Delete loan failed", e);
        }
    }
}
