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

    static final int INSERT_LOAN_ACCOUNT_NO = 1;
    static final int INSERT_CUSTOMER_ID = 2;
    static final int INSERT_LOAN_TYPE = 3;
    static final int INSERT_PRINCIPAL_AMOUNT = 4;
    static final int INSERT_INTEREST_RATE = 5;
    static final int INSERT_TENURE_MONTHS = 6;
    static final int INSERT_STATUS = 7;
    static final int INSERT_CREATED_DATE = 8;

    static final int DELETE_LOAN_ID = 1;

    static final int UPDATE_LOAN_TYPE = 1;
    static final int UPDATE_PRINCIPAL_AMOUNT = 2;
    static final int UPDATE_INTEREST_RATE = 3;
    static final int UPDATE_TENURE_MONTHS = 4;
    static final int UPDATE_STATUS = 5;
    static final int UPDATE_LOAN_ID = 6;

    Logger LOG = LoggerFactory.getLogger(LoanDao.class);

    String InsertSQL =
        "INSERT INTO loan (loan_account_no, customer_id, loan_type, principal_amount, interest_rate, tenure_months, status, created_date) "
      + "VALUES (?,?,?,?,?,?,?,?)";

    String SelectAllSQL =
        "SELECT * FROM loan";

    String SelectByIdSQL =
        "SELECT * FROM loan WHERE id=?";

    String UpdateSQL =
        "UPDATE loan SET loan_type=?, principal_amount=?, interest_rate=?, tenure_months=?, status=? WHERE id=?";

    String DeleteSQL =
        "DELETE FROM loan WHERE id=?";

    public void insert(Loan loan) {

        try (Connection con = ConnectionClass.getConnection();
             PreparedStatement ps = con.prepareStatement(InsertSQL)) {

            ps.setString(INSERT_LOAN_ACCOUNT_NO, loan.getLoanAccountNo());
            ps.setLong(INSERT_CUSTOMER_ID, loan.getCustomerId());
            ps.setString(INSERT_LOAN_TYPE, loan.getLoanType());
            ps.setBigDecimal(INSERT_PRINCIPAL_AMOUNT, loan.getPrincipalAmount());
            ps.setBigDecimal(INSERT_INTEREST_RATE, loan.getInterestRate());
            ps.setInt(INSERT_TENURE_MONTHS, loan.getTenureMonths());
            ps.setString(INSERT_STATUS, loan.getStatus());
            ps.setTimestamp(INSERT_CREATED_DATE, loan.getCreatedDate());

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
             PreparedStatement ps = con.prepareStatement(SelectAllSQL);
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
             PreparedStatement ps = con.prepareStatement(SelectByIdSQL)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

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

        } catch (SQLException e) {
            throw new DataException("Fetch loan failed", e);
        }

        return loan;
    }

    public void update(Loan loan) {

        try (Connection con = ConnectionClass.getConnection();
             PreparedStatement ps = con.prepareStatement(UpdateSQL)) {

            ps.setString(UPDATE_LOAN_TYPE, loan.getLoanType());
            ps.setBigDecimal(UPDATE_PRINCIPAL_AMOUNT, loan.getPrincipalAmount());
            ps.setBigDecimal(UPDATE_INTEREST_RATE, loan.getInterestRate());
            ps.setInt(UPDATE_TENURE_MONTHS, loan.getTenureMonths());
            ps.setString(UPDATE_STATUS, loan.getStatus());
            ps.setLong(UPDATE_LOAN_ID, loan.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DataException("Update loan failed", e);
        }
    }

    public void delete(long id) {

        try (Connection con = ConnectionClass.getConnection();
             PreparedStatement ps = con.prepareStatement(DeleteSQL)) {

            ps.setLong(DELETE_LOAN_ID, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DataException("Delete loan failed", e);
        }
    }
}

