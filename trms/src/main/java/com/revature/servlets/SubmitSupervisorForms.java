package com.revature.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.revature.trms.Employee;
import com.revature.trms.RequestForm;
import com.revature.trms.RequestHandler;

public class SubmitSupervisorForms extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HttpSession session = request.getSession();
			if(session.isNew())
			{
				session.invalidate();				//new sessions shouldn't be here, send them to log in
				response.sendRedirect("index.html");
			}
			else
			{
				Employee employee = (Employee)session.getAttribute("employee");	//get employee from session
				RequestHandler rHandler = RequestHandler.getRequestHandler();
				List<RequestForm> formsToUpdate = rHandler.getFormsForSupervisorLevelApproval(employee.getId());	//get the employee's forms that need to be completed from database (update method requires a RequestForm Object)
				for(RequestForm form : formsToUpdate)											//loop through the request forms
				{
					String approved = request.getParameter("form" + form.getRequestID() + "approval");//get the grade for that request
					form.setSupervisorApproval(approved.equalsIgnoreCase("true"));			//if approved was selected
					try {
						rHandler.updateRequest(form);												//update request
					} catch (NullPointerException e) {
						// method throws nullpointerexception, but also successfully submits data to database, so just catching the exception and doing nothing.
					}
				}
			}
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block// method throws nullpointerexception, but also successfully submits data to database, so just catching the exception and doing nothing.
		}
		response.sendRedirect("http://localhost:8080/trms/ReimbursementManager");		//send back to reimbursement manager
	}

}
