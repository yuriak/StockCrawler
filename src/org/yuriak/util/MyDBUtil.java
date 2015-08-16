package org.yuriak.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.yuriak.config.CommonValue;

public class MyDBUtil {
	public Connection getConnection() throws Exception{
		Connection connection;
		Class.forName(CommonValue.DRIVER_NAME);
		return connection=DriverManager.getConnection(CommonValue.DB_URL, CommonValue.DB_USERNAME, CommonValue.DB_PASSWORD);	
	}
}
