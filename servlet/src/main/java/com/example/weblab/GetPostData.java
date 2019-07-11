package com.example.weblab;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

public class GetPostData extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private void processRequest(HttpServletRequest request,
								HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");

		try (PrintWriter out = response.getWriter()) {
			String url = request.getParameter("url");
			Connection.Response res = Jsoup.connect(url).execute();
			out.println(res.body());

			out.println("<html>");
			out.println("<head>");
			out.println("<title>显示Header信息</title>");
			out.println("</head>");
			out.println("<body bgcolor=\"FDF5E6\">");
			out.println("<h1 align=\"center\">Hello, " + request.getParameter("username") + "~</h1>");
			out.println("<table border=1 align=\"center\">");
			out.println("<tr bgcolor=\"#FFAD00\">");
			out.println("<th>Header Name<th>Header Value");

			Enumeration headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String headerName = (String) headerNames.nextElement();
				out.println("<tr><td>" + headerName);
				out.println("<td>" + request.getHeader(headerName));
			}

			out.println("</table>");
			out.println("</body>");
			out.println("</html>");
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		processRequest(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		processRequest(request,response);
	}
	
	public String getServletInfo() {
		return "Short description";
	}

}
