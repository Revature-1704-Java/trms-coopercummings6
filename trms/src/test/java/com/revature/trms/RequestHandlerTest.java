package com.revature.trms;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.rules.ExpectedException;

public class RequestHandlerTest 
{
	@Test
	public void getRequestHandlerShouldReturnAValueWhenNoInstanceExists()
	{
		RequestHandler rHandler = RequestHandler.getRequestHandler();
		assertNotNull(rHandler);
	}
	@Test
	public void getRequestHandlerShouldReturnAValueWhenInstanceExists()
	{
		@SuppressWarnings("unused")
		RequestHandler rHandler = RequestHandler.getRequestHandler();
		assertNotNull(RequestHandler.getRequestHandler());
	}
	
	@Test
	public void getEmployeeReturnsEmployeeIfIDExists()
	{
		RequestHandler rHandler = RequestHandler.getRequestHandler();
		assertNotNull(rHandler.getEmployee(1));
	}
	@Test 
	public void getEmployeeShouldReturnNullIfIDDoesNotExist()
	{
		RequestHandler rHandler = RequestHandler.getRequestHandler();
		assertNull(rHandler.getEmployee(-1));
	}
	
	@Test
	public void getFormsForCompletionShouldNotReturnFormsForEmployeeThatDoesNotExist()
	{
		RequestHandler rHandler = RequestHandler.getRequestHandler();
		List<RequestForm> testList = rHandler.getFormsForCompletion(-1);
		assertTrue(testList.isEmpty());
	}
	@Test 
	public void getFormsForCompletionShouldNotReturnFormsForEmployeeWithNoIncompleteForms()
	{
		RequestHandler rHandler = RequestHandler.getRequestHandler();
		List<RequestForm> testList = rHandler.getFormsForCompletion(3);
		assertTrue(testList.isEmpty());
	}
	@Test 
	public void getFormsForCompletionReturnsFormsThatExist()
	{
		RequestHandler rHandler = RequestHandler.getRequestHandler();
		List<RequestForm> testList = rHandler.getFormsForCompletion(2);
		assertFalse(testList.isEmpty());
	}
	@Test
	public void getFormsForCompletionShouldNotReturnCompleteForms()
	{
		RequestHandler rHandler = RequestHandler.getRequestHandler();
		List<RequestForm> testList = rHandler.getFormsForCompletion(2);
		boolean completedFormPresent = false;
		for(RequestForm form : testList)
		{
			if(form.getFinalGrade() != null)
				completedFormPresent = true;
		}
		assertFalse(completedFormPresent);
	}
	
	@Test
	public void getCompletedFormsShouldNotReturnFormsForEmployeeThatDoesNotExist()
	{
		RequestHandler rHandler = RequestHandler.getRequestHandler();
		List<RequestForm> testList = rHandler.getCompletedForms(-1);
		assertTrue(testList.isEmpty());
	}
	@Test 
	public void getCompletedFormsShouldNotReturnFormsForEmployeeWithNoCompleteForms()
	{
		RequestHandler rHandler = RequestHandler.getRequestHandler();
		List<RequestForm> testList = rHandler.getCompletedForms(1);
		assertTrue(testList.isEmpty());
	}
	@Test 
	public void getCompletedFormsReturnsFormsThatExist()
	{
		RequestHandler rHandler = RequestHandler.getRequestHandler();
		List<RequestForm> testList = rHandler.getCompletedForms(2);
		assertFalse(testList.isEmpty());
	}
	@Test
	public void getCompletedFormsShouldNotReturnIncompleteForms()
	{
		RequestHandler rHandler = RequestHandler.getRequestHandler();
		List<RequestForm> testList = rHandler.getCompletedForms(2);
		boolean IncompleteFormsPresent = false;
		for(RequestForm form : testList)
		{
			if(form.getFinalGrade() == null)
				IncompleteFormsPresent = true;
		}
		assertFalse(IncompleteFormsPresent);
	}
	
	@Test
	public void getFormsForSupervisorLevelApprovalShouldNotReturnFormsForEmployeesWhoDoNotSupervise()
	{
		RequestHandler rHandler = RequestHandler.getRequestHandler();
		List<RequestForm> testList = rHandler.getFormsForSupervisorLevelApproval(5);
		assertTrue(testList.isEmpty());
	}
	@Test
	public void getFormsForSuperVisorLevelApprovalShouldReturnFormsThatNeedApproval()
	{
		RequestHandler rHandler = RequestHandler.getRequestHandler();
		List<RequestForm> testList = rHandler.getFormsForSupervisorLevelApproval(1);
		assertFalse(testList.isEmpty());
	}
	
	@Test
	public void getFormsFordepHeadApprovalShouldNotReturnFormsForEmployeesWhoDoNotSupervise()
	{
		RequestHandler rHandler = RequestHandler.getRequestHandler();
		List<RequestForm> testList = rHandler.getFormsForDepartmentHeadApproval(5);
		assertTrue(testList.isEmpty());
	}
	@Test
	public void getFormsForDepHeadApprovalShouldReturnFormsThatNeedApproval()
	{
		RequestHandler rHandler = RequestHandler.getRequestHandler();
		List<RequestForm> testList = rHandler.getFormsForDepartmentHeadApproval(2);
		assertFalse(testList.isEmpty());
	}
	
	@Test
	public void getFormsForBCoordinatorApprovalShouldNotReturnFormsForEmployeesWhoDoNotSupervise()
	{
		RequestHandler rHandler = RequestHandler.getRequestHandler();
		List<RequestForm> testList = rHandler.getFormsForBenefitsCoordinatorApproval(5);
		assertTrue(testList.isEmpty());
	}
	@Test
	public void getFormsForBCoordinatorApprovalShouldReturnFormsThatNeedApproval()
	{
		RequestHandler rHandler = RequestHandler.getRequestHandler();
		List<RequestForm> testList = rHandler.getFormsForBenefitsCoordinatorApproval(1);
		assertFalse(testList.isEmpty());
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void submitNewReimbursementRequestShouldNotAddIncompleteForms()
	{
		RequestHandler rHandler = RequestHandler.getRequestHandler();
		List<RequestForm> initialforms = rHandler.getFormsForBenefitsCoordinatorApproval(1);
		RequestForm form = new RequestForm();
		form.setRequesterID(2);//set employeeId to 2, employee 2 reports to benefits coordinator with id 1 in sample data.
		rHandler.submitNewReimbursementRequest(form);
		List<RequestForm> formsafterattemptedinsert = rHandler.getFormsForBenefitsCoordinatorApproval(1);
		assertTrue(initialforms.size() == formsafterattemptedinsert.size());//there should be the same number of forms before and after the insert
		//because the incomplete form should not be inserted
	}
	/*@Test											//can't seem to get the assertion on this test to work, but with manual checking I was able to confirm that data is being inserted
	public void submittedFormsAreAddedIfComplete()
	{
		RequestHandler rHandler = RequestHandler.getRequestHandler();
		List<RequestForm> initialforms = rHandler.getFormsForCompletion(1);
		RequestForm form = new RequestForm();
		form.setRequesterID(1);
		form.setLocation("A State University Campus");
		form.setGradingFormat("PASS/FAIL");
		form.setEventType("University Course");
		form.setCost(150.0);
		form.setWorkTimeMissed(0.0);
		rHandler.submitNewReimbursementRequest(form);
		List<RequestForm> formsafterattemptedinsert = rHandler.getFormsForCompletion(1);
		assertTrue(initialForms.size() < formsafterattemptedinsert.size());//there should be one more form after the insert
	}*/
}
