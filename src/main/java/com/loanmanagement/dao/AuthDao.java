package com.loanmanagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.loanmanagement.dbconfig.ConnectionClass;
import com.loanmanagement.exception.DataException;

public class AuthDao {

    private static final Logger LOG =
            LoggerFactory.getLogger(AuthDao.class);

    private static final String VALIDATE_LOGIN_SQL =
            "SELECT CUSTOMER_ID FROM USER_CREDENTIAL WHERE USERNAME=? AND PASSWORD=?";

    private static final String INSERT_TOKEN_SQL =
            "INSERT INTO AUTH_TOKEN (CUSTOMER_ID, TOKEN, CREATED_DATE) VALUES (?, ?, CURRENT_TIMESTAMP)";

    private static final String VALIDATE_TOKEN_SQL =
            "SELECT COUNT(*) FROM AUTH_TOKEN WHERE TOKEN=?";

    public long validateLogin(String username, String hashedPassword) {

        try (Connection con = ConnectionClass.getConnection();
             PreparedStatement ps = con.prepareStatement(VALIDATE_LOGIN_SQL)) {

            ps.setString(1, username);
            ps.setString(2, hashedPassword);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getLong("CUSTOMER_ID") : 0;
            }

        } catch (SQLException e) {
            LOG.error("Login validation failed for user {}", username, e);
            throw new DataException("Login validation failed", e);
        }
    }

    public void saveToken(long customerId, String token) {

        try (Connection con = ConnectionClass.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_TOKEN_SQL)) {

            ps.setLong(1, customerId);
            ps.setString(2, token);
            ps.executeUpdate();

        } catch (SQLException e) {
            LOG.error("Token save failed for customer {}", customerId, e);
            throw new DataException("Token save failed", e);
        }
    }

    public boolean isTokenValid(String token) {

        try (Connection con = ConnectionClass.getConnection();
             PreparedStatement ps = con.prepareStatement(VALIDATE_TOKEN_SQL)) {

            ps.setString(1, token);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            LOG.error("Token validation failed", e);
            throw new DataException("Token validation failed", e);
        }
    }
}
