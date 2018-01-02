package com.revature.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ReimbursementManager
 */
public class ReimbursementManager extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		HttpSession session = request.getSession();
		if(session.isNew())
		{
			session.invalidate();
			response.sendRedirect("index.html");
		}
		else
		{
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			
			out.println("<!DOCTYPE html>");
			out.println("<html>");
			out.println("	<head>");
			out.println("		<meta charset=\"utf-8\">");
			out.println("		<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">");
			out.println("		<title>Tuition Reimbursement System</title>");
			out.println("		<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\">");
			out.println("		<script src=\"https://code.jquery.com/jquery-3.1.1.slim.min.js\" integrity=\"sha384-A7FZj7v+d/sdmMqp/nOQwliLvUsJfDHW+k9Omg/a/EheAdgtzNs3hpfag6Ed950n\" crossorigin=\"anonymous\"></script>");
			out.println("		<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js\"></script>");
			out.println("		<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\"></script>");
			out.println("	</head>");
			out.println("	<body>");
			out.println("		<div class=\"container\">");
			out.println("			<div class=\"jumbotron\">");
			out.println("				<h1 class=\"h1-responsive\">Submit a new reimbursement request form</h1>"); 
			out.println("				<hr class=\"my-2\">");
			out.println("				<a class=\"btn btn-primary btn-lg\" data-toggle=\"modal\" data-target=\"#requestForm\">Start Form</a>");
			out.println("				<div class=\"modal fade\" id=\"requestForm\" tabindex=\"-1\" role=\"dialog\"");
			out.println("				     aria-labelledby=\\\"requestFormLabel\\\" aria-hidden=\\\"true\\\">");
			out.println("				    <div class=\"modal-dialog\">");
			out.println("				        <div class=\"modal-content\">");
			out.println("				            <!-- Modal Header -->");
			out.println("				            <div class=\"modal-header\">");
			out.println("				                <button type=\"button\" class=\"close\" data-dismiss=\"modal\">");
			out.println("				                       <span aria-hidden=\"true\">&times;</span>");
			out.println("				                       <span class=\"sr-only\">Close</span>");
			out.println("				                </button>");
			out.println("				                <h4 class=\"modal-title\" id=\"requestFormLabel\">");
			out.println("				                    Tuition Reimbursement Request Form");
			out.println("				                </h4>");
			out.println("							</div>");
			out.println("				            <!-- Modal Body -->");
			out.println("				            <div class=\"modal-body\">");
			out.println("				                <form class=\"form-horizontal\" role=\"form\" method=\"post\" action=\"http://localhost:8080/trmsFrontEnd/SubmitForm\">");
			out.println("				                  <div class=\"form-group\">");
			out.println("				                    <label  class=\"col-sm-2 control-label\" for=\"Location\">Location:</label>");
			out.println("				                    <div class=\"col-sm-10\">");
			out.println("				                        <input type=\"text\" class=\"form-control\" id=\"Location\" placeholder=\"Location\"/ name=\"Location\">");
			out.println("				                    </div>");
			out.println("				                  </div>");
			out.println("				                  <div class=\"form-group\">				");
			out.println("								    <label  class=\"col-sm-2 control-label\" for=\"gradingFormat\">Grading Format:</label>");
			out.println("								    <div class=\"col-sm-10\">");
			out.println("										<input name=\"gradingFormat\" type=\"radio\" id=\"PASS/FAIL\">");
			out.println("										<label for=\"PASS/FAIL\">Pass/Fail</label>");
			out.println("								    </div>");
			out.println("								  </div>");
			out.println("				                  <div class=\"form-group\">");
			out.println("								    <div class=\"col-sm-10\">");
			out.println("										<input name=\"gradingFormat\" type=\"radio\" id=\"A/B/C/D/F_D_PASS\">");
			out.println("										<label for=\"A/B/C/D/F_D_PASS\">A/B/C/D/F D passing</label>");
			out.println("								    </div>");
			out.println("								  </div>");
			out.println("				                  <div class=\"form-group\">");
			out.println("								    <div class=\"col-sm-10\">");
			out.println("										<input name=\"gradingFormat\" type=\"radio\" id=\"A/B/C/D/F_C_PASS\">");
			out.println("										<label for=\"A/B/C/D/F_C_PASS\">A/B/C/D/F C passing</label>");
			out.println("								    </div>");
			out.println("								  </div>");
			out.println("				                  <div class=\"form-group\">");
			out.println("								    <div class=\"col-sm-10\">");
			out.println("										<input name=\"gradingFormat\" type=\"radio\" id=\"OTHER\">");
			out.println("										<label for=\"OTHER\">Other</label>");
			out.println("								    </div>");
			out.println("								  </div>");
			out.println("				                  <div class=\"form-group\">				");
			out.println("								    <label  class=\"col-sm-2 control-label\" for=\"eventType\">Event Type:</label>");
			out.println("								    <div class=\"col-sm-10\">");
			out.println("										<input name=\"eventType\" type=\"radio\" id=\"universityCourse\">");
			out.println("										<label for=\"universityCourse\">University Course</label>");
			out.println("								    </div>");
			out.println("								  </div>");
			out.println("				                  <div class=\"form-group\">				");
			out.println("								    <div class=\"col-sm-10\">");
			out.println("										<input name=\"eventType\" type=\"radio\" id=\"seminar\">");
			out.println("										<label for=\"seminar\">Seminar</label>");
			out.println("								    </div>");
			out.println("								  </div>");
			out.println("				                  <div class=\"form-group\">				");
			out.println("								    <div class=\"col-sm-10\">");
			out.println("										<input name=\"eventType\" type=\"radio\" id=\"certificationPrep\">");
			out.println("										<label for=\"certificationPrep\">Certification Preparation Class</label>");
			out.println("								    </div>");
			out.println("								  </div>");
			out.println("				                  <div class=\"form-group\">				");
			out.println("								    <div class=\"col-sm-10\">");
			out.println("										<input name=\"eventType\" type=\"radio\" id=\"certification\">");
			out.println("										<label for=\"certification\">Certification</label>");
			out.println("								    </div>");
			out.println("								  </div>");
			out.println("				                  <div class=\"form-group\">				");
			out.println("								    <div class=\"col-sm-10\">");
			out.println("										<input name=\"eventType\" type=\"radio\" id=\"technicalTraining\">");
			out.println("										<label for=\"technicalTraining\">Technical Training</label>");
			out.println("								    </div>");
			out.println("								  </div>");
			out.println("				                  <div class=\"form-group\">				");
			out.println("								    <div class=\"col-sm-10\">");
			out.println("										<input name=\"eventType\" type=\"radio\" id=\"other\">");
			out.println("										<label for=\"other\">Other</label>");
			out.println("								    </div>");
			out.println("								  </div>");
			out.println("				                  <div class=\"form-group\">");
			out.println("				                    <label  class=\"col-sm-2 control-label\" for=\"Description\">Description:</label>");
			out.println("				                    <div class=\"col-sm-10\">");
			out.println("				                        <input type=\"text\" class=\"form-control\" id=\"Description\" placeholder=\"Description\" name=\"Description\"/>");
			out.println("				                    </div>");
			out.println("				                  </div>");
			out.println("				                  <div class=\"form-group\">");
			out.println("				                    <label  class=\"col-sm-2 control-label\" for=\"Cost\">Cost:</label>");
			out.println("				                    <div class=\"col-sm-10\">");
			out.println("				                        <input type=\"number\" class=\"form-control\" id=\"Cost\" placeholder=\"Cost\" name=\"Cost\"/>");
			out.println("				                    </div>");
			out.println("				                  </div>");
			out.println("				                  <div class=\"form-group\">");
			out.println("				                    <label  class=\"col-sm-2 control-label\" for=\"workTimeMissed\">Work time missed:</label>");
			out.println("				                    <div class=\"col-sm-10\">");
			out.println("				                        <input type=\"number\" class=\"form-control\" id=\"workTimeMissed\" placeholder=\"Work time missed\" name=\"WorkTimeMissed\"/>");
			out.println("				                    </div>");
			out.println("				                  </div>");
			out.println("                  <div class=\"form-group\">");
			out.println("                    <div class=\"col-sm-offset-2 col-sm-10\">");
			out.println("						<button type=\"submit\" class=\"btn btn-default\">Submit Form</button>");
			out.println("                    </div>");
			out.println("                  </div>");
			out.println("                </form>");
			out.println("			</div>");
			out.println("");
			out.println("");
			out.println("");
			out.println("");
			out.println("");
			out.println("");
			out.println("");
			out.println("");
			out.println("");
			out.println("");
			out.println("");
			out.println("");
			out.println("");
			out.println("");
			out.println("");
			out.println("    	</div> <!-- /container -->	");
			out.println("	</body>");
			out.println("</html>");
			
			out.close();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
