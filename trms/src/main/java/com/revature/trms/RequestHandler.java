package com.revature.trms;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.util.ConnectionUtil;

public class RequestHandler 
{
	private static RequestHandler instance = null;
	
	private RequestHandler() {}//prevent default constructor from being called
	public static RequestHandler getRequestHandler()
	{
		if(instance == null)
		{
			instance = new RequestHandler();
		}
		return instance;
	}
	
	//methods to implement
	//update a form 
	/*
	 * Update an existing request form to add finalgrade, supervisorapproval, depheadapproval, bcoordinatorapproval, or denialreason.
	 * only the fields needing to be updated should be changed.
	 * @param formForUpdate a requestform object which has had the required changes made to it. This should be a form pulled from the
	 * database with only the needed fields altered.
	 */
	public void updateRequest(RequestForm formForUpdate) throws IllegalArgumentException
	{
		if (formForUpdate.getRequestID() == 0)//0 is default value for the field, IDs start at 1, cannot update a form that does not exist
		{
			throw new IllegalArgumentException();
		}
		
		PreparedStatement pStatement = null;
		try(Connection connection = ConnectionUtil.getConnection())
		{
			String sql = "UPDATE REQUEST SET FINALGRADE = ?, FINALTIMESTAMP = SYSTIMESTAMP, SupervisorApproval = ?, DepHeadApproval = ?,"
					+ " BCoordinatorApproval = ?, DenialReason = ? WHERE Request_ID = ?";
			pStatement = connection.prepareStatement(sql);
			if(formForUpdate.getFinalGrade() != null)
				pStatement.setString(1, formForUpdate.getFinalGrade());
			else
				pStatement.setNull(1, java.sql.Types.VARCHAR);
			if(formForUpdate.isSupervisorApproval() != null)
				pStatement.setBoolean(2, formForUpdate.isSupervisorApproval());
			else
				pStatement.setNull(2, java.sql.Types.CHAR);
			if(formForUpdate.isDepHeadApproval() != null)
				pStatement.setBoolean(3, formForUpdate.isDepHeadApproval());
			else
				pStatement.setNull(3, java.sql.Types.CHAR);
			if(formForUpdate.isbCoordinatorApproval() != null)
				pStatement.setBoolean(4, formForUpdate.isbCoordinatorApproval());
			else
				pStatement.setNull(4, java.sql.Types.CHAR);
			if(formForUpdate.getDenialReason() != null)
				pStatement.setString(5, formForUpdate.getDenialReason());
			else
				pStatement.setNull(5, java.sql.Types.VARCHAR);
			pStatement.setInt(6, formForUpdate.getRequestID());
			pStatement.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		finally
		{
			if(pStatement != null)
			{
				try
				{
					pStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		
	}
	
	/*
	 * Submit a RequestForm object to the database. The following fields must be initialized for this to work:
	 * requesterID, location, gradingFormat, eventType, cost, workTimeMissed, and description and 
	 * attachmentPath should be initialized if applicable. The method will not add the form if the required fields are not
	 * initialized.
	 * @param formToSubmit a RequestForm object with the above fields initialized. Other fields will be ignored because
	 * they should not be used on a new submission or are not needed.
	 */
	
	public void submitNewReimbursementRequest(RequestForm formToSubmit) throws IllegalArgumentException
	{
		//check that required fields are initialized
		if(formToSubmit.getRequesterID() != 0 && formToSubmit.getLocation() != null 
				&& formToSubmit.getGradingFormat() != null && formToSubmit.getEventType() != null 
				&& formToSubmit.getCost() != -1 && formToSubmit.getWorkTimeMissed() != -1)
		{
			//submit the form
			
			PreparedStatement pStatement = null;

			try(Connection connection = ConnectionUtil.getConnection())
			{
				//add location if it does not already exist
				String sql = "INSERT INTO LOCATION (Location_Name) SELECT ? FROM DUAL WHERE NOT EXISTS (SELECT NULL FROM Location where Location.Location_Name = ?)";
				pStatement = connection.prepareStatement(sql);
				pStatement.setString(1, formToSubmit.getLocation());
				pStatement.setString(2, formToSubmit.getLocation());
				pStatement.executeUpdate();
				
				//Insert attachment path, it should be unique, so an insertion will always be necessary
				if(formToSubmit.getAttachmentPath() != null)
				{
					sql = "INSERT INTO ATTACHMENT (Attachment_ID, AttachmentPath) Values (0, ?)";
					pStatement.setString(1, formToSubmit.getAttachmentPath());
					pStatement.executeUpdate();
				}
				
				//add row to request table
				StringBuilder sqlquery = new StringBuilder();
				sqlquery.append("INSERT INTO REQUEST (Requester_ID, EventLocation_ID,");
				sqlquery.append("GradingFormat_ID, EventType_ID, Description, Cost, WorkTimeMissed, Attachment_ID)");
				sqlquery.append(" VALUES (?, (SELECT Location_ID FROM LOCATION WHERE Location_Name = ?), ");
				sqlquery.append("(SELECT Format_ID FROM GradingFormat WHERE FormatName = ?), (SELECT Type_ID FROM EVENTTYPE WHERE TYPENAME = ?),");
				sqlquery.append(" ?, ?, ?, (SELECT MAX(Attachment_ID) FROM Attachment WHERE AttachmentPath = ?))");//max will return null if there are no rows, the subquery will only have 0 or 1 row because attachmentpath is unique
				pStatement.close();
				pStatement = connection.prepareStatement(sqlquery.toString());
				pStatement.setInt(1, formToSubmit.getRequesterID());
				pStatement.setString(2, formToSubmit.getLocation());
				pStatement.setString(3, formToSubmit.getGradingFormat());
				pStatement.setString(4, formToSubmit.getEventType());
				if(formToSubmit.getDescription() != null)
					pStatement.setString(5, formToSubmit.getDescription());
				else
					pStatement.setNull(5, java.sql.Types.VARCHAR);
				pStatement.setDouble(6, formToSubmit.getCost());
				pStatement.setDouble(7, formToSubmit.getWorkTimeMissed());
				if(formToSubmit.getAttachmentPath() != null)
					pStatement.setString(8, formToSubmit.getAttachmentPath());
				else
					pStatement.setNull(8, java.sql.Types.VARCHAR);
				pStatement.executeUpdate();
			}
			catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (ClassNotFoundException e2) {
				e2.printStackTrace();
			}
			finally
			{
				if(pStatement != null)
				{
					try
					{
						pStatement.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		else
		{
			throw new IllegalArgumentException();
		}
	}
	
	/*
	 * Get forms for employee to approve at benefits coordinator level (they may fulfill multiple roles)
	 * @param bcoodinatorID the id of the department head whom forms are being retrieved for
	 * @return List of forms that require approval
	 */
	public List<RequestForm> getFormsForBenefitsCoordinatorApproval(int BCoordinatorID)
	{
		List<RequestForm> forms = new ArrayList<>();
		ResultSet rSet = null;
		PreparedStatement pStatement = null;
		try(Connection connection = ConnectionUtil.getConnection())
		{
			String sql = "Select * FROM REQUEST INNER JOIN LOCATION ON EventLocation_ID = LOCATION.LOCATION_ID\r\n" + 
					"INNER JOIN GRADINGFORMAT ON REQUEST.GRADINGFORMAT_ID = GRADINGFORMAT.FORMAT_ID\r\n" + 
					"INNER JOIN EVENTTYPE ON REQUEST.EVENTTYPE_ID = EVENTTYPE.TYPE_ID\r\n" + 
					"INNER JOIN EMPLOYEE ON REQUEST.REQUESTER_ID = EMPLOYEE.EMPLOYEE_ID\r\n" + 
					"LEFT JOIN ATTACHMENT ON REQUEST.ATTACHMENT_ID = ATTACHMENT.ATTACHMENT_ID WHERE EMPLOYEE.BCOORDINATORID = ? AND BCoordinatorApproval IS NULL";
			pStatement = connection.prepareStatement(sql);
			pStatement.setInt(1, BCoordinatorID);
			rSet = pStatement.executeQuery();
			while(rSet.next())
			{
				RequestForm temp = new RequestForm();
				temp.setFinalDate(rSet.getTimestamp("FinalTimestamp"));
				temp.setFinalGrade(rSet.getString("FinalGrade"));
				temp.setWorkTimeMissed(rSet.getDouble("WorkTimeMissed"));
				temp.setSupervisorApproval(rSet.getBoolean("SupervisorApproval"));
				if(rSet.wasNull())
					temp.setSupervisorApproval(null);
				temp.setRequestID(rSet.getInt("Request_ID"));
				temp.setRequesterID(rSet.getInt("Requester_ID"));
				temp.setLocation(rSet.getString("Location_Name"));
				temp.setGradingFormat(rSet.getString("FormatName"));
				temp.setEventType(rSet.getString("TypeName"));
				temp.setDescription(rSet.getString("Description"));
				temp.setDepHeadApproval(rSet.getBoolean("DepHeadApproval"));
				if(rSet.wasNull())
					temp.setDepHeadApproval(null);
				temp.setDenialReason(rSet.getString("DenialReason"));
				temp.setDateSubmitted(rSet.getTimestamp("DateTimeSubmitted"));
				temp.setCost(rSet.getDouble("Cost"));
				temp.setbCoordinatorApproval(rSet.getBoolean("BCoordinatorApproval"));
				if(rSet.wasNull())
					temp.setbCoordinatorApproval(null);
				temp.setAttachmentPath(rSet.getString("AttachmentPath"));
				forms.add(temp);
			}
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		finally
		{
			if(rSet != null)
			{
				try
				{
					rSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pStatement != null)
			{
				try
				{
					pStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return forms;
	}
	
	/*
	 * Get forms for an employee to approve as a department head (they may fulfill multiple roles)
	 * @param depHeadID the id of the department head whom forms are being retrieved for
	 * @return List of forms that require approval
	 */
	public List<RequestForm> getFormsForDepartmentHeadApproval(int depHeadID)
	{
		List<RequestForm> forms = new ArrayList<>();
		ResultSet rSet = null;
		PreparedStatement pStatement = null;
		try(Connection connection = ConnectionUtil.getConnection())
		{
			String sql = "Select * FROM REQUEST INNER JOIN LOCATION ON EventLocation_ID = LOCATION.LOCATION_ID\r\n" + 
					"INNER JOIN GRADINGFORMAT ON REQUEST.GRADINGFORMAT_ID = GRADINGFORMAT.FORMAT_ID\r\n" + 
					"INNER JOIN EVENTTYPE ON REQUEST.EVENTTYPE_ID = EVENTTYPE.TYPE_ID\r\n" + 
					"INNER JOIN EMPLOYEE ON REQUEST.REQUESTER_ID = EMPLOYEE.EMPLOYEE_ID\r\n" + 
					"LEFT JOIN ATTACHMENT ON REQUEST.ATTACHMENT_ID = ATTACHMENT.ATTACHMENT_ID WHERE EMPLOYEE.DEPHEADID = ? AND DepHeadApproval IS NULL";
			pStatement = connection.prepareStatement(sql);
			pStatement.setInt(1, depHeadID);
			rSet = pStatement.executeQuery();
			while(rSet.next())
			{
				RequestForm temp = new RequestForm();
				temp.setFinalDate(rSet.getTimestamp("FinalTimestamp"));
				temp.setFinalGrade(rSet.getString("FinalGrade"));
				temp.setWorkTimeMissed(rSet.getDouble("WorkTimeMissed"));
				temp.setSupervisorApproval(rSet.getBoolean("SupervisorApproval"));
				if(rSet.wasNull())
					temp.setSupervisorApproval(null);
				temp.setRequestID(rSet.getInt("Request_ID"));
				temp.setRequesterID(rSet.getInt("Requester_ID"));
				temp.setLocation(rSet.getString("Location_Name"));
				temp.setGradingFormat(rSet.getString("FormatName"));
				temp.setEventType(rSet.getString("TypeName"));
				temp.setDescription(rSet.getString("Description"));
				temp.setDepHeadApproval(rSet.getBoolean("DepHeadApproval"));
				if(rSet.wasNull())
					temp.setDepHeadApproval(null);
				temp.setDenialReason(rSet.getString("DenialReason"));
				temp.setDateSubmitted(rSet.getTimestamp("DateTimeSubmitted"));
				temp.setCost(rSet.getDouble("Cost"));
				temp.setbCoordinatorApproval(rSet.getBoolean("BCoordinatorApproval"));
				if(rSet.wasNull())
					temp.setbCoordinatorApproval(null);
				temp.setAttachmentPath(rSet.getString("AttachmentPath"));
				forms.add(temp);
			}
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		finally
		{
			if(rSet != null)
			{
				try
				{
					rSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pStatement != null)
			{
				try
				{
					pStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return forms;
	}
	
	/*
	 * Get forms for an employee to approve as a supervisor (they may fill multiple roles)
	 * @param supervisorID The id of the supervisor whom forms are bing retrieved for
	 * @return List of forms that require approval
	 */
	public List<RequestForm> getFormsForSupervisorLevelApproval(int supervisorID)
	{
		List<RequestForm> forms = new ArrayList<>();
		ResultSet rSet = null;
		PreparedStatement pStatement = null;
		try(Connection connection = ConnectionUtil.getConnection())
		{
			String sql = "Select * FROM REQUEST INNER JOIN LOCATION ON EventLocation_ID = LOCATION.LOCATION_ID\r\n" + 
					"INNER JOIN GRADINGFORMAT ON REQUEST.GRADINGFORMAT_ID = GRADINGFORMAT.FORMAT_ID\r\n" + 
					"INNER JOIN EVENTTYPE ON REQUEST.EVENTTYPE_ID = EVENTTYPE.TYPE_ID\r\n" + 
					"INNER JOIN EMPLOYEE ON REQUEST.REQUESTER_ID = EMPLOYEE.EMPLOYEE_ID\r\n" + 
					"LEFT JOIN ATTACHMENT ON REQUEST.ATTACHMENT_ID = ATTACHMENT.ATTACHMENT_ID WHERE EMPLOYEE.SUPERVISORID = ? AND SUPERVISORAPPROVAL IS NULL";
			pStatement = connection.prepareStatement(sql);
			pStatement.setInt(1, supervisorID);
			rSet = pStatement.executeQuery();
			while(rSet.next())
			{
				RequestForm temp = new RequestForm();
				temp.setFinalDate(rSet.getTimestamp("FinalTimestamp"));
				temp.setFinalGrade(rSet.getString("FinalGrade"));
				temp.setWorkTimeMissed(rSet.getDouble("WorkTimeMissed"));
				temp.setSupervisorApproval(rSet.getBoolean("SupervisorApproval"));
				if(rSet.wasNull())
					temp.setSupervisorApproval(null);
				temp.setRequestID(rSet.getInt("Request_ID"));
				temp.setRequesterID(rSet.getInt("Requester_ID"));
				temp.setLocation(rSet.getString("Location_Name"));
				temp.setGradingFormat(rSet.getString("FormatName"));
				temp.setEventType(rSet.getString("TypeName"));
				temp.setDescription(rSet.getString("Description"));
				temp.setDepHeadApproval(rSet.getBoolean("DepHeadApproval"));
				if(rSet.wasNull())
					temp.setDepHeadApproval(null);
				temp.setDenialReason(rSet.getString("DenialReason"));
				temp.setDateSubmitted(rSet.getTimestamp("DateTimeSubmitted"));
				temp.setCost(rSet.getDouble("Cost"));
				temp.setbCoordinatorApproval(rSet.getBoolean("BCoordinatorApproval"));
				if(rSet.wasNull())
					temp.setbCoordinatorApproval(null);
				temp.setAttachmentPath(rSet.getString("AttachmentPath"));
				forms.add(temp);
			}
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		finally
		{
			if(rSet != null)
			{
				try
				{
					rSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pStatement != null)
			{
				try
				{
					pStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return forms;
	}
	
	/*
	 * Retrieve the forms which an employee has already filled out completely so that their status can be reviewed
	 * @param targetID the emploeeID of the employee whose forms are to be retrieved
	 * @return a List of forms that do not need to be completed by the employee. May be empty if the employee has never completed a form
	 */
	public List<RequestForm> getCompletedForms(int TargetID)
	{
		List<RequestForm> forms = new ArrayList<>();
		ResultSet rSet = null;
		PreparedStatement pStatement = null;
		try(Connection connection = ConnectionUtil.getConnection())
		{
			String sql = "Select * FROM REQUEST INNER JOIN LOCATION ON EventLocation_ID = LOCATION.LOCATION_ID\r\n" + 
					"INNER JOIN GRADINGFORMAT ON REQUEST.GRADINGFORMAT_ID = GRADINGFORMAT.FORMAT_ID\r\n" + 
					"INNER JOIN EVENTTYPE ON REQUEST.EVENTTYPE_ID = EVENTTYPE.TYPE_ID\r\n" + 
					"LEFT JOIN ATTACHMENT ON REQUEST.ATTACHMENT_ID = ATTACHMENT.ATTACHMENT_ID WHERE Requester_ID = ? AND FINALGRADE IS NOT NULL";
			pStatement = connection.prepareStatement(sql);
			pStatement.setInt(1, TargetID);
			rSet = pStatement.executeQuery();
			
			while(rSet.next())
			{
				RequestForm temp = new RequestForm();
				temp.setFinalDate(rSet.getTimestamp("FinalTimestamp"));
				temp.setFinalGrade(rSet.getString("FinalGrade"));
				temp.setWorkTimeMissed(rSet.getDouble("WorkTimeMissed"));
				temp.setSupervisorApproval(rSet.getBoolean("SupervisorApproval"));
				if(rSet.wasNull())
					temp.setSupervisorApproval(null);
				temp.setRequestID(rSet.getInt("Request_ID"));
				temp.setRequesterID(TargetID);
				temp.setLocation(rSet.getString("Location_Name"));
				temp.setGradingFormat(rSet.getString("FormatName"));
				temp.setEventType(rSet.getString("TypeName"));
				temp.setDescription(rSet.getString("Description"));
				temp.setDepHeadApproval(rSet.getBoolean("DepHeadApproval"));
				if(rSet.wasNull())
					temp.setDepHeadApproval(null);
				temp.setDenialReason(rSet.getString("DenialReason"));
				temp.setDateSubmitted(rSet.getTimestamp("DateTimeSubmitted"));
				temp.setCost(rSet.getDouble("Cost"));
				temp.setbCoordinatorApproval(rSet.getBoolean("BCoordinatorApproval"));
				if(rSet.wasNull())
					temp.setbCoordinatorApproval(null);
				temp.setAttachmentPath(rSet.getString("AttachmentPath"));
				forms.add(temp);
			}
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		finally
		{
			if(rSet != null)
			{
				try
				{
					rSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pStatement != null)
			{
				try
				{
					pStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return forms;
	}
	
	/*
	 * Retrieve forms which need final grade to be entered by employee
	 * @param targetID the employeeID of the employee whose forms need to be retrieved
	 * @return a List of forms to be reviewed. May be empty if there are no forms to complete.
	 */
	public List<RequestForm> getFormsForCompletion(int TargetID)
	{
		List<RequestForm> forms = new ArrayList<>();
		ResultSet rSet = null;
		PreparedStatement pStatement = null;
		
		try(Connection connection = ConnectionUtil.getConnection())
		{
			String sql = "Select * FROM REQUEST INNER JOIN LOCATION ON EventLocation_ID = LOCATION.LOCATION_ID\r\n" + 
					"INNER JOIN GRADINGFORMAT ON REQUEST.GRADINGFORMAT_ID = GRADINGFORMAT.FORMAT_ID\r\n" + 
					"INNER JOIN EVENTTYPE ON REQUEST.EVENTTYPE_ID = EVENTTYPE.TYPE_ID\r\n" + 
					"LEFT JOIN ATTACHMENT ON REQUEST.ATTACHMENT_ID = ATTACHMENT.ATTACHMENT_ID WHERE Requester_ID = ? AND FINALGRADE IS NULL";
			pStatement = connection.prepareStatement(sql);
			pStatement.setInt(1, TargetID);
			rSet = pStatement.executeQuery();
			
			while(rSet.next())
			{
				RequestForm temp = new RequestForm();
				temp.setWorkTimeMissed(rSet.getDouble("WorkTimeMissed"));
				temp.setSupervisorApproval(rSet.getBoolean("SupervisorApproval"));
				if(rSet.wasNull())
					temp.setSupervisorApproval(null);
				temp.setRequestID(rSet.getInt("Request_ID"));
				temp.setRequesterID(TargetID);
				temp.setLocation(rSet.getString("Location_Name"));
				temp.setGradingFormat(rSet.getString("FormatName"));
				temp.setEventType(rSet.getString("TypeName"));
				temp.setDescription(rSet.getString("Description"));
				temp.setDepHeadApproval(rSet.getBoolean("DepHeadApproval"));
				if(rSet.wasNull())
					temp.setDepHeadApproval(null);
				temp.setDenialReason(rSet.getString("DenialReason"));
				temp.setDateSubmitted(rSet.getTimestamp("DateTimeSubmitted"));
				temp.setCost(rSet.getDouble("Cost"));
				temp.setbCoordinatorApproval(rSet.getBoolean("BCoordinatorApproval"));
				if(rSet.wasNull())
					temp.setbCoordinatorApproval(null);
				temp.setAttachmentPath(rSet.getString("AttachmentPath"));
				forms.add(temp);
			}
		
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		finally
		{
			if(rSet != null)
			{
				try
				{
					rSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pStatement != null)
			{
				try
				{
					pStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return forms;
	}
	
	/*
	 * Retrieve information about an employee from the database
	 * @param targetID The employeeId of the employee to retrieve
	 * @return an Employee bean containing the relevant information.
	 */
	public Employee getEmployee(int targetID)
	{
		Employee retrievedEmployee = null;
		ResultSet rSet = null;
		PreparedStatement pStatement = null;
		
		try(Connection connection = ConnectionUtil.getConnection())
		{
			//get employee with matching ID
			String sql = "SELECT * FROM EMPLOYEE INNER JOIN EMPLOYEETYPE ON Employee.EmployeeType_ID = EmployeeType.EmployeeType_ID WHERE Employee_ID = ?";
			pStatement = connection.prepareStatement(sql);
			pStatement.setInt(1, targetID);
			rSet = pStatement.executeQuery();
			if(rSet.next())
			{
				retrievedEmployee = new Employee();
				retrievedEmployee.setId(targetID);
				retrievedEmployee.setName(rSet.getString("EmployeeName"));
				retrievedEmployee.setClaimsAmountRemaining(rSet.getDouble("ClaimsAmountRemaining"));
				retrievedEmployee.seteMail(rSet.getString("eMail"));
				retrievedEmployee.setPassword(rSet.getString("Password"));
				retrievedEmployee.setEmployeeTypeID(rSet.getInt("EmployeeType_ID"));
				retrievedEmployee.setEmployeeType(rSet.getString("EmployeeTypeTitle"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e2) {
			e2.printStackTrace();
		}
		finally
		{
			if(pStatement != null)
			{
				try {
					pStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(rSet != null)
			{
				try
				{
					rSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return retrievedEmployee;
	}
}
