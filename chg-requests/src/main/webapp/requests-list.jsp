<%@page import="java.util.Iterator"%>
<%@page import="es.chg.portal.requests.db.TemplateVO"%>
<%@page import="java.util.List"%>
<%@page import="es.chg.portal.requests.db.RequestsDAO"%>
<%
response.setHeader("Cache-Control","no-cache"); 
response.setHeader("Pragma","no-cache"); 
response.setDateHeader("Expires", -1); 
%>
<%
	if (request.getParameter("identifier") != null && !request.getParameter("identifier").trim().isEmpty()) {
		String userId = RequestsDAO.getUserIdByIdentifier(request.getParameter("identifier"));
		if (userId != null) {
			session.setAttribute("chg-requests-id-user", userId);
		} else {
			session.removeAttribute("chg-requests-id-user");
		}
	}
%>
<?xml version="1.0" encoding="ISO-8859-1" ?>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
	<title>CHG Listado de Solicitudes</title>
	<link rel="stylesheet" type="text/css" href="styles/base.css" media="screen" />
	
	<link rel="stylesheet" type="text/css" href="js/yui_2.9.0/fonts/fonts-min.css" />
	<script src="js/yui_2.9.0/yahoo-dom-event/yahoo-dom-event.js"></script>
	<script src="js/yui_2.9.0/element/element-min.js"></script>

	<link rel="stylesheet" type="text/css" href="js/yui_2.9.0/datatable/assets/skins/sam/datatable.css" />
	<script src="js/yui_2.9.0/dragdrop/dragdrop-min.js"></script>
	<script src="js/yui_2.9.0/datasource/datasource-min.js"></script>
	<script src="js/yui_2.9.0/datatable/datatable-min.js"></script>

	<link rel="stylesheet" type="text/css" href="js/yui_2.9.0/paginator/assets/skins/sam/paginator.css" />
	<script src="js/yui_2.9.0/paginator/paginator-min.js"></script>
</head>
<body id="body-page-rl" class="yui-skin-sam">
<%
	if (session.getAttribute("chg-requests-id-user") == null) {
%>
	<div class="alert">Sesión expirada</div>
<% } else { %>
	<div id="section-nr" class="section">
		<div class="title-section">Nueva Solicitud</div>
		<div class="body-section">
			<form id="form-nr" name="form-nr" action="load-weboffice.jsp" method="post">
				<div class="form-fields">
					<div class="form-field">
						<label for="request-type">Tipo de Solicitud:</label>
						<select name="request-type" id="request-type">
							<% 
							List<TemplateVO> templates = RequestsDAO.getListTemplates();
							Iterator<TemplateVO> itTemplates = templates.iterator();
							while(itTemplates.hasNext()) { 
								TemplateVO template = itTemplates.next();
							%>
							<option value="<%=template.getId()%>"><%=template.getName()%></option>
							<%
							}
							%>
						</select>
					</div>
				</div>
				<div class="form-buttons"><button type="submit" name="create-request" id="create-request">Crear Solicitud</button></div>
			</form>
		</div>
	</div>
	<div id="section-rl" class="section">
		<div class="title-section">Historial de Solicitudes</div>
		<div class="body-section">
			<div id="rl-table"></div>
 			<div id="rl-pag"></div>
		</div>
	</div>
	<div id="section-a" class="section">
		<div class="title-section">Acciones</div>
		<div class="body-section">
			<form id="form-a" name="form-a" action="RequestActionsServlet" method="post">
				<div class="form-buttons">
					<button type="submit" name="sync-pf" id="sync-pf">Sincronizar con Port@firmas</button>
				</div>
			</form>
		</div>
	</div>
	<script>
		function renderRequestsGrid() {
			
	 		YAHOO.widget.DataTable.Formatter.formatStatus = function(elLiner, oRecord, oColumn, oData) {
	       	 	var status = oRecord.getData("status");
	       	 	
	       	 	if (status == 'PENDIENTE_FIRMA') {
	       	 		status = 'Pendiente de Firma';
	       	 	} else if (status == 'FIRMADO') {
	       	 		status = 'Firmado';
	       	 	} else if (status == 'PENDIENTE_APROBACION') {
	       	 		status = 'Pendiente de Aprobación';
	       	 	} else if (status == 'APROBADO') {
	       	 		status = 'Aprobado';
	       	 	} else if (status == 'RECHAZADO') {
	       	 		status = 'Rechazado';
	       	 	}
	       	 	
	       	 	elLiner.innerHTML = status;
		    };
		    
			// Formato para las acciones
	 		YAHOO.widget.DataTable.Formatter.formatActions = function(elLiner, oRecord, oColumn, oData) {
	       	 	elLiner.innerHTML += '<a href="request-detail.jsp?id-request=' + oRecord.getData("id") + '">Ver detalle</a>';
		    };
			
	        var columnDefs = new Array();
	        columnDefs.push({key:"id", label:"ID", sortable:true, resizeable:true});
			columnDefs.push({key:"name_template", label:"Tipo", sortable:true, resizeable:true});
			columnDefs.push({key:"register_date", label:"Fecha de Creación", sortable:true, resizeable:true});
			columnDefs.push({key:"status", label:"Estado", formatter:"formatStatus", sortable:true, resizeable:true});
			columnDefs.push({key:"actions", label:"Acciones", formatter:"formatActions"});
			
	        var dataSource = new YAHOO.util.DataSource(<%=RequestsDAO.getListRequestsByUserJSON((String)session.getAttribute("chg-requests-id-user")) %>);
	        dataSource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY;
	        dataSource.responseSchema = { 
	            fields: ["id", "register_date", "status", "name_template"] 
	        };
	        
	        dataSource = new YAHOO.widget.DataTable("rl-table", columnDefs, dataSource, 
	                { MSG_EMPTY: "No hay registros", MSG_ERROR: "Se ha producido un error", MSG_LOADING: "Cargando...",
	 				  paginator : new YAHOO.widget.Paginator(
	                	{ rowsPerPage: 9, alwaysVisible: false, containers: 'rl-pag', pageLinks: 25,
	                	  firstPageLinkLabel: "<<", lastPageLinkLabel: ">>", previousPageLinkLabel: "<", nextPageLinkLabel: ">"
	                    }),
	                  draggableColumns: true } 
	        );
		}
	
		YAHOO.util.Event.onDOMReady(function() {
			renderRequestsGrid();
		});
	</script>
<% } %>
</body>
</html>