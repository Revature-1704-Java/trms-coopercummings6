package com.revature.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.revature.trms.Employee;
import com.revature.trms.RequestHandler;

public class LogIn extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("index.html");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		HttpSession session = request.getSession();
		RequestHandler rHandler = RequestHandler.getRequestHandler();
		Employee employee = rHandler.getEmployee(Integer.parseInt(request.getParameter("employeeID")));
		if(employee.getPassword().equals(request.getParameter("password")))
		{
			session.setAttribute("employee", employee);
			response.sendRedirect("http://localhost:8080/trmsFrontEnd/ReimbursementManager");
		} 
		else 
		{
			session.invalidate();
			response.sendRedirect("index.html");
		}
	}
}
