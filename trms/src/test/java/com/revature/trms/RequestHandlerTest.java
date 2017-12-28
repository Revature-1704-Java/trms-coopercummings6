package com.revature.trms;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

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
		List<RequestForm> testList = rHandler.getFormsForCompletion(1);
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
}
