<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*"%>
<%@ page session="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>session jsp</title>
</head>
<body>
	<%
		Cookie[] cookies = request.getCookies();
		if(cookies==null){
			out.println("no cookie");
			return;
		}
		for(Cookie cookie:cookies){
	%>
		<p>cookie name:<%= cookie.getName() %></p>
		<p>cookie value:<%= cookie.getValue() %></p>
		<p>cookie max age:<%= cookie.getMaxAge() %></p>
		
	<%
		}
	%>
	<% 
		HttpSession session = request.getSession();
		Enumeration<String> enumer = session.getAttributeNames();
     while(enumer.hasMoreElements()) 
     {
	%>			
      <p><%= enumer.nextElement() %></p>
   <%
     } 
   %>
    <p><%= session.getServletContext() %></p>
    <p><%= session.getServletContext().getContextPath() %></p>
    <p><%= session.getId() %></p>
    <p><%= session.getMaxInactiveInterval() %></p>
</body>
</html>