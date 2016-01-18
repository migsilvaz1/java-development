<%
response.setHeader("Cache-Control","no-cache"); 
response.setHeader("Pragma","no-cache"); 
response.setDateHeader("Expires", -1); 
%>
<%
String url = request.getRequestURL().toString();
String baseURL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath();
%>
<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
	<title>CHG Load WebOffice</title>
	<link rel="stylesheet" type="text/css" href="styles/base.css" media="screen" />
	
	<link rel="stylesheet" type="text/css" href="js/yui_2.9.0/fonts/fonts-min.css" />
	<script src="js/yui_2.9.0/yahoo-dom-event/yahoo-dom-event.js"></script>
	<script src="js/yui_2.9.0/element/element-min.js"></script>
</head>
<body id="body-page-lw">
<%
	if (session.getAttribute("chg-requests-id-user") == null) {
%>
	<div class="alert">Sesión expirada</div>
<% } else { %>
	<div>
		<p>No cierre esta ventana hasta que salga de WebOffice</p>
		<form id="form-lw" action="requests-list.jsp" method="post">
			<button type="submit" name="form-lw-submit" id="form-lw-submit">Volver</button>
		</form>
	</div>
	<div>
	  	<applet id="WebOffice" archive="<%=baseURL %>/weboffice/OOProject.jar" code="WebOffice" name ="WebOffice">
	  		<param name="urlXML" value="<%=baseURL %>/OOConfigXMLServlet?idTemplate=<%=request.getParameter("request-type") %>"></param>
	  		<param name="urlComponentes" value="<%=baseURL %>/weboffice/ComponentesWebOffice.jar"></param>
	  		Si no visualiza el applet WebOffice debe instalar el jre
	  	</applet>
  	</div>
<% } %>
</body>
</html>