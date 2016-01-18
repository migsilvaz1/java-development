<%
response.setHeader("Cache-Control","no-cache"); 
response.setHeader("Pragma","no-cache"); 
response.setDateHeader("Expires", -1); 
%>
<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
	<title>CHG Error</title>
	<link rel="stylesheet" type="text/css" href="styles/base.css" media="screen" />
	
	<link rel="stylesheet" type="text/css" href="js/yui_2.9.0/fonts/fonts-min.css" />
	<script src="js/yui_2.9.0/yahoo-dom-event/yahoo-dom-event.js"></script>
	<script src="js/yui_2.9.0/element/element-min.js"></script>
</head>
<body id="body-page-error" class="yui-skin-sam">
	<div id="section-error" class="section">
		<div class="title-section">Error</div>
		<div class="body-section">
			<form id="form-error" name="form-error" action="RequestActionsServlet" method="post">
				<%
				if (request.getParameter("id-request") != null) {
				%>
				<input name="id-request" id="id-request" type="hidden" value="<%=request.getParameter("id-request") %>" />
				<%
				}
				%>
				<div class="form-field">
					<label for="error-message">Descripción del error:</label>
					<%
						String errorId = request.getParameter("id-error");
						if ("0".equals(errorId)) {
							%>Se ha producido un error no controlado.<%
						} else if ("1".equals(errorId)) {
							%>Error en la validación del certificado.<%
						} else if ("2".equals(errorId)) {
							%>El NIF del certificado no coincide con el NIF del usuario autenticado.<%
						} else if ("3".equals(errorId)) {
							%>Certificado no válido.<%
						} else if ("4".equals(errorId)) {
							%>No existe ningún usuario registrado con esas credenciales.<%
						}
					%>
				</div>
				<div class="form-buttons">
					<%
					if (request.getParameter("id-request") != null) {
					%>
					<button type="submit" name="return-detail" id="return-detail">Volver</button>
					<%
					}
					%>
					<%
					if (request.getParameter("source") != null) {
					%>
					<button type="button" name="return-source" id="return-source" onclick="location.href='<%=request.getParameter("source")%>'">Volver</button>
					<%
					}
					%>
				</div>
			</form>
		</div>
	</div>
</body>
</html>