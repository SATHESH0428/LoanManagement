package com.loanmanagement.dbconfig;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.loanmanagement.exception.DataException;

public class ConnectionClass {

    private ConnectionClass() {
    }

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new DataException("MySQL Driver not found", e);
        }
    }

    public static Connection getConnection() {

        Properties prop = new Properties();

        try (InputStream inputStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("db.properties")) {

            if (inputStream == null) {
                throw new DataException("db.properties file NOT FOUND in classpath");
            }

            prop.load(inputStream);

            String url = prop.getProperty("db.URL");
            String user = prop.getProperty("db.USER");
            String password = prop.getProperty("db.PASSWORD");

            return DriverManager.getConnection(url, user, password);

        } catch (SQLException e) {
            throw new DataException("DB connection failed", e);
        } catch (Exception e) {
            throw new DataException("Error loading database configuration", e);
        }
    }
}
