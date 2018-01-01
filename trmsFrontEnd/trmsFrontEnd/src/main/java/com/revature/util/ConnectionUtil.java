package com.revature.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil 
{
	public static Connection getConnection() throws SQLException, IOException, ClassNotFoundException
	{
		String url = "jdbc:oracle:thin:@trmsdatabase.cuabhte2vcds.us-east-2.rds.amazonaws.com:1521:ORCL";
		String user = "coopercummings6";
		String password = "Cooperc9";
		
		Class.forName("oracle.jdbc.driver.OracleDriver");
		
		return DriverManager.getConnection(url, user, password);
	}//end getConnection
}//end ConnectionUtil class
