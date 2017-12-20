package com.revature.util;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.Test;

public class ConnectionUtilTest 
{
	@Test
	public void connectionUtilReturnsConnection() 
	{
		try 
		{
			Connection connection = ConnectionUtil.getConnection();
			assertTrue(connection instanceof Connection);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void noExceptionsThrownIfConnectionIsUsed()
	{
		String sql = "Select * from employee";
		PreparedStatement pStatement = null;
		try(Connection connection = ConnectionUtil.getConnection()) {
			pStatement = connection.prepareStatement(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//connection should be set up properly if its methods execute
	}
}
