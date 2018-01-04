package com.revature.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.revature.trms.Employee;
import com.revature.trms.RequestForm;
import com.revature.trms.RequestHandler;

public class getCompleteForms extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
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
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.serializeNulls();
			Gson gson = gsonBuilder.create();
			Employee employee = (Employee)session.getAttribute("employee");
			RequestHandler rHandler = RequestHandler.getRequestHandler();
			List<RequestForm> incompleteForms = rHandler.getCompletedForms(employee.getId());
			String jsonForms = gson.toJson(incompleteForms);
			PrintWriter out = response.getWriter();
			response.setContentType("application/json");
		    response.setCharacterEncoding("UTF-8");
		    out.print(jsonForms);
		    out.close(); 
		}
	}

}
