package com.loanmanagement.daotest;
import com.loanmanagement.dao.AuthDao;
import com.loanmanagement.exception.DataException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class AuthDaoTest {

    private AuthDao authDao;

    @BeforeEach
    void setup() throws Exception {

        authDao = new AuthDao();

        try (Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/loan_management",
                "root",
                "Admin@123")) {

            Statement stmt = con.createStatement();
            stmt.execute("DELETE FROM AUTH_TOKEN");
            stmt.execute("DELETE FROM USER_CREDENTIAL");

         
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO USER_CREDENTIAL (CUSTOMER_ID, USERNAME, PASSWORD) VALUES (?, ?, ?)");
            ps.setLong(1, 1L);
            ps.setString(2, "testuser");
            ps.setString(3, "testpass");
            ps.executeUpdate();
        }
    }

   
    @Test
    void testValidateLogin_success() {

        long customerId = authDao.validateLogin("testuser", "testpass");

        assertEquals(1L, customerId);
    }

    
    @Test
    void testValidateLogin_invalid() {

        long customerId = authDao.validateLogin("wronguser", "wrongpass");

        assertEquals(0L, customerId);
    }

  
    @Test
    void testTokenValidation_success() {

        authDao.saveToken(1L, "token123");

        boolean valid = authDao.isTokenValid("token123");

        assertTrue(valid);
    }

    
    @Test
    void testTokenValidation_invalid() {

        boolean valid = authDao.isTokenValid("invalidToken");

        assertFalse(valid);
    }
}
