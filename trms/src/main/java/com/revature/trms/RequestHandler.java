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
	
	//submit a form
	//update a form
	
	/*
	 * Submit a RequestForm object to the database. The following fields must be initialized for this to work:
	 * requesterID, dateSubmitted, location, gradingFormat, eventType, cost, workTimeMissed, and description and 
	 * attachmentPath should be initialized if applicable. The method will not add the form if the required fields are not
	 * initialized.
	 * @param formToSubmit a RequestForm object with the above fields initialized. Other fields will be ignored because
	 * they should not be used on a new submission or are not needed.
	 */
	//public void submitNewReimbursementRequest(RequestForm formToSubmit)
	//{
	//	//check that required fields are initialized
	//	if(formToSubmit.getRequesterID() != 0 && formToSubmit.getDateSubmitted() != null
	//			&& formToSubmit.getLocation() != null && formToSubmit.getGradingFormat() != null
	//			&& formToSubmit.getEventType() != null && formToSubmit.getCost() != 0.0d
	//			&& formToSubmit.getWorkTimeMissed() != 0.0d)
	//	{
	//		//submit the form
	//	}
	//}
	
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
				temp.setFinalDate(rSet.getDate("FinalTimestamp"));
				temp.setFinalGrade(rSet.getString("FinalGrade"));
				temp.setWorkTimeMissed(rSet.getDouble("WorkTimeMissed"));
				temp.setSupervisorApproval(rSet.getBoolean("SupervisorApproval"));
				temp.setRequestID(rSet.getInt("Request_ID"));
				temp.setRequesterID(rSet.getInt("Requester_ID"));
				temp.setLocation(rSet.getString("Location_Name"));
				temp.setGradingFormat(rSet.getString("FormatName"));
				temp.setEventType(rSet.getString("TypeName"));
				temp.setDescription(rSet.getString("Description"));
				temp.setDepHeadApproval(rSet.getBoolean("DepHeadApproval"));
				temp.setDenialReason(rSet.getString("DenialReason"));
				temp.setDateSubmitted(rSet.getDate("DateTimeSubmitted"));
				temp.setCost(rSet.getDouble("Cost"));
				temp.setbCoordinatorApproval(rSet.getBoolean("BCoordinatorApproval"));
				temp.setAttachmentPath(rSet.getString("AttachmentPath"));
				forms.add(temp);
			}
		} catch (SQLException | IOException e) {
			e.printStackTrace();
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
				temp.setFinalDate(rSet.getDate("FinalTimestamp"));
				temp.setFinalGrade(rSet.getString("FinalGrade"));
				temp.setWorkTimeMissed(rSet.getDouble("WorkTimeMissed"));
				temp.setSupervisorApproval(rSet.getBoolean("SupervisorApproval"));
				temp.setRequestID(rSet.getInt("Request_ID"));
				temp.setRequesterID(rSet.getInt("Requester_ID"));
				temp.setLocation(rSet.getString("Location_Name"));
				temp.setGradingFormat(rSet.getString("FormatName"));
				temp.setEventType(rSet.getString("TypeName"));
				temp.setDescription(rSet.getString("Description"));
				temp.setDepHeadApproval(rSet.getBoolean("DepHeadApproval"));
				temp.setDenialReason(rSet.getString("DenialReason"));
				temp.setDateSubmitted(rSet.getDate("DateTimeSubmitted"));
				temp.setCost(rSet.getDouble("Cost"));
				temp.setbCoordinatorApproval(rSet.getBoolean("BCoordinatorApproval"));
				temp.setAttachmentPath(rSet.getString("AttachmentPath"));
				forms.add(temp);
			}
		} catch (SQLException | IOException e) {
			e.printStackTrace();
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
				temp.setFinalDate(rSet.getDate("FinalTimestamp"));
				temp.setFinalGrade(rSet.getString("FinalGrade"));
				temp.setWorkTimeMissed(rSet.getDouble("WorkTimeMissed"));
				temp.setSupervisorApproval(rSet.getBoolean("SupervisorApproval"));
				temp.setRequestID(rSet.getInt("Request_ID"));
				temp.setRequesterID(rSet.getInt("Requester_ID"));
				temp.setLocation(rSet.getString("Location_Name"));
				temp.setGradingFormat(rSet.getString("FormatName"));
				temp.setEventType(rSet.getString("TypeName"));
				temp.setDescription(rSet.getString("Description"));
				temp.setDepHeadApproval(rSet.getBoolean("DepHeadApproval"));
				temp.setDenialReason(rSet.getString("DenialReason"));
				temp.setDateSubmitted(rSet.getDate("DateTimeSubmitted"));
				temp.setCost(rSet.getDouble("Cost"));
				temp.setbCoordinatorApproval(rSet.getBoolean("BCoordinatorApproval"));
				temp.setAttachmentPath(rSet.getString("AttachmentPath"));
				forms.add(temp);
			}
		} catch (SQLException | IOException e) {
			e.printStackTrace();
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
				temp.setFinalDate(rSet.getDate("FinalTimestamp"));
				temp.setFinalGrade(rSet.getString("FinalGrade"));
				temp.setWorkTimeMissed(rSet.getDouble("WorkTimeMissed"));
				temp.setSupervisorApproval(rSet.getBoolean("SupervisorApproval"));
				temp.setRequestID(rSet.getInt("Request_ID"));
				temp.setRequesterID(TargetID);
				temp.setLocation(rSet.getString("Location_Name"));
				temp.setGradingFormat(rSet.getString("FormatName"));
				temp.setEventType(rSet.getString("TypeName"));
				temp.setDescription(rSet.getString("Description"));
				temp.setDepHeadApproval(rSet.getBoolean("DepHeadApproval"));
				temp.setDenialReason(rSet.getString("DenialReason"));
				temp.setDateSubmitted(rSet.getDate("DateTimeSubmitted"));
				temp.setCost(rSet.getDouble("Cost"));
				temp.setbCoordinatorApproval(rSet.getBoolean("BCoordinatorApproval"));
				temp.setAttachmentPath(rSet.getString("AttachmentPath"));
				forms.add(temp);
			}
		} catch (SQLException | IOException e) {
			e.printStackTrace();
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
				temp.setRequestID(rSet.getInt("Request_ID"));
				temp.setRequesterID(TargetID);
				temp.setLocation(rSet.getString("Location_Name"));
				temp.setGradingFormat(rSet.getString("FormatName"));
				temp.setEventType(rSet.getString("TypeName"));
				temp.setDescription(rSet.getString("Description"));
				temp.setDepHeadApproval(rSet.getBoolean("DepHeadApproval"));
				temp.setDenialReason(rSet.getString("DenialReason"));
				temp.setDateSubmitted(rSet.getDate("DateTimeSubmitted"));
				temp.setCost(rSet.getDouble("Cost"));
				temp.setbCoordinatorApproval(rSet.getBoolean("BCoordinatorApproval"));
				temp.setAttachmentPath(rSet.getString("AttachmentPath"));
				forms.add(temp);
			}
		
		} catch (SQLException | IOException e) {
			e.printStackTrace();
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
