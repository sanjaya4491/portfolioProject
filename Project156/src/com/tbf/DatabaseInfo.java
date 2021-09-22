package com.tbf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *	Class that holds information to connect to a database
 */

public class DatabaseInfo {
	
	public static final String url = "jdbc:mysql://cse.unl.edu/sdhakal?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatatimeCode=false&serverTimezone=UTC";
	public static final String username = "sdhakal";
	public static final String password = "y6vZmRmP";
	
	/**
	 * Method that returns the connection to a database
	 * @return
	 */
	public static Connection databaseConnector() {
		
		String DRIVER_CLASS = "com.mysql.cj.jdbc.Driver";
		try {
			Class.forName(DRIVER_CLASS).getDeclaredConstructor().newInstance();
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
		
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		return conn;
	}

}
