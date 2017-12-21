package com.revature.trms;

import static org.junit.Assert.assertNotNull;

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
		RequestHandler rHandler = RequestHandler.getRequestHandler();
		assertNotNull(RequestHandler.getRequestHandler());
	}
}
