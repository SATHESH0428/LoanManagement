package com.loanmanagement.dbconfig;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import com.loanmanagement.exception.DataException;

public class ConnectionClass {

    private ConnectionClass() {}

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new DataException("MySQL Driver not found", e);
        }
    }

    public static Connection getConnection() {
        try {
            Properties prop = new Properties();

            InputStream inputStream = Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream("db.properties");

            
            if (inputStream == null) {
                throw new RuntimeException("db.properties file NOT FOUND in classpath");
            }

            prop.load(inputStream);

            String url = prop.getProperty("db.URL");
            String user = prop.getProperty("db.USER");
            String password = prop.getProperty("db.PASSWORD");

           
            System.out.println("DB URL: " + url);
            System.out.println("DB USER: " + user);

            return DriverManager.getConnection(url, user, password);

        } catch (Exception e) {
            e.printStackTrace(); 
            throw new DataException("DB not connected", e);
        }
    }
}
