package com.revature.trms;

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
	
	//get forms for supervisor to approve
	//get forms for dep head to approve
	//get forms for bcoordinator to approve
	//get employee's completed forms
	//get employee's incomplete forms
	//submit a form
	//retrieve a password
}
