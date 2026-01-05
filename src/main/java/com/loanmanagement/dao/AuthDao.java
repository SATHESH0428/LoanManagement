package com.loanmanagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.loanmanagement.dbconfig.ConnectionClass;

public class AuthDao {

    public long validateLogin(String username, String hashedPassword) {

        String sql =
            "SELECT CUSTOMER_ID FROM USER_CREDENTIAL WHERE USERNAME=? AND PASSWORD=?";

        try (Connection con = ConnectionClass.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, hashedPassword);

            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getLong("CUSTOMER_ID") : 0;

        } catch (Exception e) {
            throw new RuntimeException("Login validation failed", e);
        }
    }

    public void saveToken(long customerId, String token) {

        String sql =
            "INSERT INTO AUTH_TOKEN (CUSTOMER_ID, TOKEN, CREATED_DATE) VALUES (?, ?, CURRENT_TIMESTAMP)";

        try (Connection con = ConnectionClass.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, customerId);
            ps.setString(2, token);
            ps.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Token save failed", e);
        }
    }

    public boolean isTokenValid(String token) {

        String sql = "SELECT COUNT(*) FROM AUTH_TOKEN WHERE TOKEN=?";

        try (Connection con = ConnectionClass.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;

        } catch (Exception e) {
            throw new RuntimeException("Token validation failed", e);
        }
    }
}
