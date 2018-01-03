package com.revature.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson;

import com.revature.trms.Employee;
import com.revature.trms.RequestForm;
import com.revature.trms.RequestHandler;

/**
 * Servlet implementation class getIncompleteForms
 */
public class getIncompleteForms extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Get should not be used, it will not be supported, users should not try to force get method
		HttpSession session = request.getSession();
		session.invalidate();
		response.sendRedirect("index.html");//log out users who try to access hidden servlets using the wrong method
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		HttpSession session = request.getSession();
		if(session.isNew())
		{
			session.invalidate();
			response.sendRedirect("index.html");
		}
		else
		{
			Gson gson = new Gson();
			Employee employee = (Employee)session.getAttribute("employee");
			RequestHandler rHandler = RequestHandler.getRequestHandler();
			List<RequestForm> incompleteForms = rHandler.getFormsForCompletion(employee.getId());
			String jsonForms = gson.toJson(incompleteForms);
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    out.print(jsonForms);
		    out.close(); 
		}
	}

}
