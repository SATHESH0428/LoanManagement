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
            "SELECT COUNT(*) AS token_count FROM AUTH_TOKEN WHERE TOKEN=?";

    private static final int LOGIN_PARAM_USERNAME = 1;
    private static final int LOGIN_PARAM_PASSWORD = 2;

    private static final int INSERT_PARAM_CUSTOMER_ID = 1;
    private static final int INSERT_PARAM_TOKEN = 2;

    private static final int VALIDATE_PARAM_TOKEN = 1;

    public long validateLogin(String username, String hashedPassword) {

        try (Connection con = ConnectionClass.getConnection();
             PreparedStatement ps = con.prepareStatement(VALIDATE_LOGIN_SQL)) {

            ps.setString(LOGIN_PARAM_USERNAME, username);
            ps.setString(LOGIN_PARAM_PASSWORD, hashedPassword);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("CUSTOMER_ID");
                }
                return 0;
            }

        } catch (SQLException e) {
            
            throw new DataException("Login validation failed for user " + username, e);
        }
    }

    public void saveToken(long customerId, String token) {

        try (Connection con = ConnectionClass.getConnection();
             PreparedStatement ps = con.prepareStatement(INSERT_TOKEN_SQL)) {

            ps.setLong(INSERT_PARAM_CUSTOMER_ID, customerId);
            ps.setString(INSERT_PARAM_TOKEN, token);
            ps.executeUpdate();

        } catch (SQLException e) {
           
            throw new DataException("Token save failed for customer " + customerId, e);
        }
    }

    public boolean isTokenValid(String token) {

        try (Connection con = ConnectionClass.getConnection();
             PreparedStatement ps = con.prepareStatement(VALIDATE_TOKEN_SQL)) {

            ps.setString(VALIDATE_PARAM_TOKEN, token);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt("token_count") > 0;
            }

        } catch (SQLException e) {
            
            throw new DataException("Token validation failed", e);
        }
    }
}
