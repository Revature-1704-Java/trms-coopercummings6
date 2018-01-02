package com.revature.servlets;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.revature.trms.Employee;
import com.revature.trms.RequestForm;
import com.revature.trms.RequestHandler;

/**
 * Servlet implementation class SubmitForm
 */
public class SubmitForm extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);//users shouldn't be calling this with get, let post deal with it
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if(session.isNew())
		{
			session.invalidate();
			response.sendRedirect("index.html");
		}
		else
		{
			RequestForm requestForm = new RequestForm();
			requestForm.setRequesterID(((Employee)session.getAttribute("employee")).getId());
			requestForm.setLocation(request.getParameter("Location"));
			requestForm.setGradingFormat(request.getParameter("gradingFormat"));
			requestForm.setEventType(request.getParameter("eventType"));
			requestForm.setDescription(request.getParameter("Description"));
			requestForm.setCost(Double.parseDouble(request.getParameter("Cost")));
			requestForm.setWorkTimeMissed(Double.parseDouble(request.getParameter("WorkTimeMissed")));
			RequestHandler requestHandler = RequestHandler.getRequestHandler();
			requestHandler.submitNewReimbursementRequest(requestForm);
			response.sendRedirect("http://localhost:8080/trmsFrontEnd/ReimbursementManager");
		}
	}

}
