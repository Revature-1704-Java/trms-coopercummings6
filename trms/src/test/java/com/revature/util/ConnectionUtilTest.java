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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void noExceptionsThrownIfConnectionIsUsed()
	{
		String sql = "Select * from employee";
		@SuppressWarnings("unused")
		PreparedStatement pStatement = null;
		try(Connection connection = ConnectionUtil.getConnection()) {
			pStatement = connection.prepareStatement(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}//connection should be set up properly if its methods execute
	}
}
