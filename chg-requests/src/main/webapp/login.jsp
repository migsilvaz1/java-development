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
	<title>CHG Acceso</title>
	<link rel="stylesheet" type="text/css" href="styles/base.css" media="screen" />
	
	<link rel="stylesheet" type="text/css" href="js/yui_2.9.0/fonts/fonts-min.css" />
	<script src="js/yui_2.9.0/yahoo-dom-event/yahoo-dom-event.js"></script>
	<script src="js/yui_2.9.0/element/element-min.js"></script>
</head>
<body id="body-page-login" class="yui-skin-sam">
	<div id="section-login" class="section">
		<div class="title-section">Acceso</div>
		<div class="body-section">
			<form id="form-login" name="form-login" action="LoginServlet" method="post">
				<input name="certificate-b64" id="certificate-b64" type="hidden" value="" />
				<div class="form-buttons">
					<button type="button" name="login-cert" id="login-cert" onclick="javascript:doLoginCert();">Acceder con Certificado</button>
				</div>
			</form>
		</div>
	</div>
	<script type="text/javascript" src="miniapplet/miniapplet.js"></script>
	<script type="text/javascript">
		function showLog(log) {
			alert(log);
		}
		
		function showResultCallback(signatureB64, certificateB64) {
			document.getElementById('certificate-b64').value = certificateB64;
			document.getElementById('form-login').submit();
		}

		function doLoginCert() {
			try {
				MiniApplet.sign(
					'1234567890',
					"SHA1withRSA",
					"CAdES",
					"filter=authcert:;nonexpired:",
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
</body>
</html>