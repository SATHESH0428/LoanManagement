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

    
    private static final int INSERT_LOAN_ACCOUNT_NO = 1;
    private static final int INSERT_CUSTOMER_ID = 2;
    private static final int INSERT_LOAN_TYPE = 3;
    private static final int INSERT_PRINCIPAL = 4;
    private static final int INSERT_INTEREST = 5;
    private static final int INSERT_TENURE = 6;
    private static final int INSERT_STATUS = 7;
    private static final int INSERT_CREATED_DATE = 8;

    private static final int SELECT_BY_ID = 1;
    private static final int DELETE_BY_ID = 1;

    private static final int UPDATE_LOAN_TYPE = 1;
    private static final int UPDATE_PRINCIPAL = 2;
    private static final int UPDATE_INTEREST = 3;
    private static final int UPDATE_TENURE = 4;
    private static final int UPDATE_STATUS = 5;
    private static final int UPDATE_ID = 6;

   
    private static final String INSERT_SQL =
            "INSERT INTO loan (loan_account_no, customer_id, loan_type, principal_amount, " +
            "interest_rate, tenure_months, status, created_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_ALL_SQL =
            "SELECT * FROM loan";

    private static final String SELECT_BY_ID_SQL =
            "SELECT * FROM loan WHERE id=?";

    private static final String UPDATE_SQL =
            "UPDATE loan SET loan_type=?, principal_amount=?, interest_rate=?, " +
            "tenure_months=?, status=? WHERE id=?";

    private static final String DELETE_SQL =
            "DELETE FROM loan WHERE id=?";

   

    public void insert(Loan loan) {

        try (Connection con = ConnectionClass.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_SQL)) {

            ps.setString(INSERT_LOAN_ACCOUNT_NO, loan.getLoanAccountNo());
            ps.setLong(INSERT_CUSTOMER_ID, loan.getCustomerId());
            ps.setString(INSERT_LOAN_TYPE, loan.getLoanType());
            ps.setBigDecimal(INSERT_PRINCIPAL, loan.getPrincipalAmount());
            ps.setBigDecimal(INSERT_INTEREST, loan.getInterestRate());
            ps.setInt(INSERT_TENURE, loan.getTenureMonths());
            ps.setString(INSERT_STATUS, loan.getStatus());
            ps.setTimestamp(INSERT_CREATED_DATE, loan.getCreatedDate());

            int rows = ps.executeUpdate();
            if (rows == 0) {
                LOG.error("Insert failed for loan {}", loan.getLoanAccountNo());
            } else {
                LOG.info("Loan inserted successfully {}", loan.getLoanAccountNo());
            }

        } catch (SQLException e) {
            throw new DataException("Failed to insert loan", e);
        }
    }

    public List<Loan> findAll() {

        List<Loan> loans = new ArrayList<>();

        try (Connection con = ConnectionClass.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                loans.add(mapping(rs));
            }

            
            return loans;

        } catch (SQLException e) {
            throw new DataException("Failed to fetch loans", e);
        }
    }

    public Loan findById(long id) {

        try (Connection con = ConnectionClass.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_BY_ID_SQL)) {

            ps.setLong(SELECT_BY_ID, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapping(rs);
                }
            }

            
            return null;

        } catch (SQLException e) {
            throw new DataException("Failed to fetch loan by id", e);
        }
    }

    public void update(Loan loan) {

        try (Connection con = ConnectionClass.getConnection();
             PreparedStatement ps = con.prepareStatement(UPDATE_SQL)) {

            ps.setString(UPDATE_LOAN_TYPE, loan.getLoanType());
            ps.setBigDecimal(UPDATE_PRINCIPAL, loan.getPrincipalAmount());
            ps.setBigDecimal(UPDATE_INTEREST, loan.getInterestRate());
            ps.setInt(UPDATE_TENURE, loan.getTenureMonths());
            ps.setString(UPDATE_STATUS, loan.getStatus());
            ps.setLong(UPDATE_ID, loan.getId());

            int rows = ps.executeUpdate();
            if (rows == 0) {
                LOG.error("Update failed for loan id={}", loan.getId());
            } else {
                LOG.info("Loan updated successfully id={}", loan.getId());
            }

        } catch (SQLException e) {
            throw new DataException("Failed to update loan", e);
        }
    }

    public void delete(long id) {

        try (Connection con = ConnectionClass.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE_SQL)) {

            ps.setLong(DELETE_BY_ID, id);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                LOG.error("Delete failed, loan not found id={}", id);
            } else {
                LOG.info("Loan deleted successfully id={}", id);
            }

        } catch (SQLException e) {
            throw new DataException("Failed to delete loan", e);
        }
    }

   
    private static Loan mapping(ResultSet rs) throws SQLException {

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
        return loan;
    }
}
