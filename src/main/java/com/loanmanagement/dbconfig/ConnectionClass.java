package com.loanmanagement.dbconfig;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import com.loanmanagement.exception.DataException;

public class ConnectionClass {
	 private ConnectionClass() {
	    }

	    static {
	        try {

	            Class.forName("com.mysql.cj.jdbc.Driver");
	        } catch (ClassNotFoundException e) {

	            throw new DataException("Driver not found");
	        }
	    }

	    public static Connection getConnection() {
	        try {
	            Properties prop =new Properties();
	            InputStream inputStream =Thread.currentThread()
	                    .getContextClassLoader()
	                    .getResourceAsStream("db.properties") ;
	            prop.load(inputStream);
	            String URL=prop.getProperty("db.URL");
	            String USER=prop.getProperty("db.USER");
	            String PASSWORD=prop.getProperty("db.PASSWORD");
	            return DriverManager.getConnection(URL, USER, PASSWORD);
	        } catch (Exception e) {
	            throw new DataException("DB not connected");
	        }
	    }


}
