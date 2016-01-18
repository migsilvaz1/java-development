<%@page import="java.util.Iterator"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="es.chg.portal.requests.RequestsUtils"%>
<%@page import="es.chg.portal.requests.db.RequestVO"%>
<%@page import="es.chg.portal.requests.db.RequestsDAO"%>
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
	<title>CHG Detalle de la Solicitud</title>
	<link rel="stylesheet" type="text/css" href="styles/base.css" media="screen" />
	
	<link rel="stylesheet" type="text/css" href="js/yui_2.9.0/fonts/fonts-min.css" />
	<script src="js/yui_2.9.0/yahoo-dom-event/yahoo-dom-event.js"></script>
	<script src="js/yui_2.9.0/element/element-min.js"></script>
</head>
<body id="body-page-rd" class="yui-skin-sam">
<%
	if (session.getAttribute("chg-requests-id-user") == null) {
%>
	<div class="alert">Sesión expirada</div>
<% } else { %>
<%
boolean isRequestOwner = RequestsDAO.isRequestOwner((String)request.getSession().getAttribute("chg-requests-id-user"), request.getParameter("id-request"));

if (isRequestOwner) {

	if (request.getParameter("init-sign") != null) {
		if ("PENDIENTE_FIRMA".equals(RequestsDAO.getRequestStatus((String)request.getSession().getAttribute("chg-requests-id-user"), request.getParameter("id-request")))) {
%>
	<div id="section-rs" class="section">
		<div class="title-section">Firma de la Solicitud #<%=request.getParameter("id-request") %></div>
		<div class="body-section">
			<object data="DownloadRequestPDFServlet?id=<%=request.getParameter("id-request") %>"></object>
			<form id="form-rs-save-sign" name="form-rs-save-sign" action="RequestActionsServlet" method="post">
				<input name="id-request" id="id-request" type="hidden" value="<%=request.getParameter("id-request") %>" />
				<input name="save-sign" id="save-sign" type="hidden" value="" />
				<input name="signature-b64" id="signature-b64" type="hidden" value="" />
				<input name="certificate-b64" id="certificate-b64" type="hidden" value="" />
			</form>
			<form id="form-rs" name="form-rs" action="RequestActionsServlet" method="post">
				<input name="id-request" id="id-request" type="hidden" value="<%=request.getParameter("id-request") %>" />
				<input name="data-sign" id="data-sign" type="hidden" value="<%=RequestsUtils.encodeBase64(RequestsDAO.getRequestPDF((String)request.getSession().getAttribute("chg-requests-id-user"), request.getParameter("id-request"))) %>" />
				<div class="form-field">
					<label for="result-sign">Resultado de firma:</label><input name="result-sign" id="result-sign" type="text"/>
				</div>
				<h3>Acciones</h3>
				<div class="form-buttons">
					<button type="button" name="sign" id="sign" onclick="javascript:doSign();">Firmar</button>
					<button type="submit" name="return-detail" id="return-detail">Volver</button>
				</div>
			</form>
		</div>
	</div>
	<script type="text/javascript" src="miniapplet/miniapplet.js"></script>
	<script type="text/javascript">
		function showLog(log) {
			document.getElementById('result-sign').value = log;
		}
		
		function showResultCallback(signatureB64, certificateB64) {
			showLog('OK');
			document.getElementById('signature-b64').value = signatureB64;
			document.getElementById('certificate-b64').value = certificateB64;
			document.getElementById('form-rs-save-sign').submit();
		}

		function doSign() {
			try {
				MiniApplet.sign(
					document.getElementById("data-sign").value,
					"SHA512withRSA",
					"PAdES",
					"filter=signingcert:;nonexpired:",
					showResultCallback,
					null);
			} catch(e) {
				try {
					showLog("Error: " + MiniApplet.getErrorMessage() + " (" + MiniApplet.getErrorType() + ")");
				} catch(ex) {
					showLog("Error: " + e);
				}
			}
		}
		
		MiniApplet.cargarMiniApplet('./miniapplet');
	</script>
<%
		}
	} else {
		RequestVO requestVO = RequestsDAO.getRequestDetail((String)request.getSession().getAttribute("chg-requests-id-user"), request.getParameter("id-request"));
%>
	<div id="section-rd" class="section">
		<div class="title-section">Detalle de la Solicitud #<%=request.getParameter("id-request") %></div>
		<div class="body-section">
			<object data="DownloadRequestDocServlet?id=<%=request.getParameter("id-request") %>"></object>
			<form id="form-rd" name="form-rd" action="RequestActionsServlet" method="post">
				<input name="id-request" id="id-request" type="hidden" value="<%=request.getParameter("id-request") %>" />
				<h3>Datos</h3>
				<div class="form-fields">
					<div class="form-field">
						<label for="request-type">Tipo:</label> <%=requestVO.getTemplate().getName() %>
					</div>
					<div class="form-field">
						<label for="register-date">Fecha de Creación:</label> <%=RequestsUtils.getFormatDate(requestVO.getRegisterDate()) %>
					</div>
					<div class="form-field">
						<label for="status">Estado:</label> <%=RequestsUtils.getFormatStatus(requestVO.getStatus()) %>
					</div>
					<% if ("PENDIENTE_APROBACION".equals(requestVO.getStatus()) || "APROBADO".equals(requestVO.getStatus()) || "RECHAZADO".equals(requestVO.getStatus())) { %>
					<%
					JSONObject pfStatusDetailedJson =JSONObject.fromObject(requestVO.getPfDetailedStatus());
					%>
					<div class="form-field">
						<label for="users-pf-status">Estado de Aprobación Detallado (Aprobador - Estado de Aprobación - Fecha de Cambio de Estado):</label> 
						<ul id="l-users-pf-status">
						<%
			   	 		for (Iterator iterator = pfStatusDetailedJson.getJSONArray("STATUS_USERS").listIterator(); iterator
								.hasNext();) {
			   	 			JSONObject statusUserJson = (JSONObject) iterator.next();
							%>
							<li><%=statusUserJson.get("user") %> - <%=RequestsUtils.getFormatStatusPf((String)statusUserJson.get("status")) %> - <%=statusUserJson.get("date") %></li>
							<%
						}
						%>
						</ul>
					</div>
					<%					
					if (!pfStatusDetailedJson.getJSONArray("COMMENTS_RETURNED").isEmpty()) {
					%>
					<div class="form-field">
						<label for="comments-returned-pf">Comentarios de Rechazo (Aprobador - Comentarios):</label> 
						<ul id="l-comments-returned-pf">
						<%
			   	 		for (Iterator iterator = pfStatusDetailedJson.getJSONArray("COMMENTS_RETURNED").listIterator(); iterator
								.hasNext();) {
			   	 			JSONObject commentReturnedJson = (JSONObject) iterator.next();
							%>
							<li><%=commentReturnedJson.get("user") %> - <%=commentReturnedJson.get("message") %></li>
							<%
						}
						%>
						</ul>
					</div>
					<% } %>
					<% } %>
				</div>
				<h3>Acciones</h3>
				<div class="form-buttons">
					<button type="button" name="download-doc" id="download-doc" onclick="window.open('DownloadRequestDocServlet?id=<%=request.getParameter("id-request") %>&mode=attachment')">Descargar Documento</button>
					<% if ("PENDIENTE_FIRMA".equals(requestVO.getStatus())) { %>
					<button type="submit" name="init-sign" id="init-sign">Iniciar Firma</button>
					<% } %>
					<% if (!"PENDIENTE_FIRMA".equals(requestVO.getStatus())) { %>
					<button type="button" name="download-signed-doc" id="download-signed-doc" onclick="window.open('DownloadRequestDocServlet?id=<%=request.getParameter("id-request") %>&mode=attachment&type=signed')">Descargar Documento Firmado</button>
					<% } %>
					<% if ("FIRMADO".equals(requestVO.getStatus())) { %>
					<button type="submit" name="send-pf" id="send-pf">Enviar a Port@firmas</button>
					<% } %>
					<% if ("APROBADO".equals(requestVO.getStatus())) { %>
					<button type="button" name="download-sign-report" id="download-sign-report" onclick="window.open('RequestActionsServlet?id-request=<%=request.getParameter("id-request") %>&download-sign-report')">Descargar Informe de Port@firmas</button>
					<% } %>
					<button type="submit" name="return-list" id="return-list">Volver</button>
				</div>
			</form>
		</div>
	</div>
<%
	}
}
%>
<% } %>
</body>
</html>