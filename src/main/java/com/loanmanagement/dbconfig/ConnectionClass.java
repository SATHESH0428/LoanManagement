package com.loanmanagement.dbconfig;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.loanmanagement.exception.DataException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ConnectionClass {

    private static final Logger LOG =
            LoggerFactory.getLogger(ConnectionClass.class);

    private static final HikariDataSource dataSource;


    static {
        try {
            LOG.info("Initializing HikariCP DataSource");

            Properties prop = new Properties();

          
            try (InputStream inputStream =
                         Thread.currentThread()
                                 .getContextClassLoader()
                                 .getResourceAsStream("db.properties")) {

                if (inputStream == null) {
                    throw new DataException("db.properties file NOT FOUND in classpath");
                }
                prop.load(inputStream);
            }

            
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(prop.getProperty("db.URL"));
            config.setUsername(prop.getProperty("db.USER"));
            config.setPassword(prop.getProperty("db.PASSWORD"));
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");

          
            config.setMaximumPoolSize(10);        
            config.setMinimumIdle(2);            
            config.setIdleTimeout(30000);        
            config.setConnectionTimeout(30000);  
            config.setPoolName("LoanManagementHikariCP");

            dataSource = new HikariDataSource(config);

            LOG.info("HikariCP DataSource initialized successfully");

        } catch (Exception e) {
            
            throw new ExceptionInInitializerError(e);
        }
    }

    
    private ConnectionClass() {
    }

   
    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new DataException("DB connection failed", e);
        }
    }
}
