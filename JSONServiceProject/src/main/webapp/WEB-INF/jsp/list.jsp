<%@page import="es.babel.db.DataCRUD"%>
<%@page import="es.babel.model.DataModel"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
	List<DataModel> datas = DataCRUD.readAllData();
%>

<div>
	<div>
		<h1>Listado de datos</h1>
		<div>
			<table>
				<tr>
					<th>userId</th>
					<th>groupId</th>
					<th>privateLayout</th>
					<th>parentLayoutId</th>
					<th>name</th>
					<th>title</th>
					<th>description</th>
					<th>keywords</th>
					<th>robots</th>
					<th>type</th>
					<th>typeSettings</th>
					<th>hidden</th>
					<th>friendlyUrl</th>
					<th></th>
				</tr>
				<%
					for (DataModel data : datas) {
				%>
				<tr>
					<td><%=data.getUserId()%></td>
					<td><%=data.getGroupId()%></td>
					<td><%=data.getPrivateLayout()%></td>
					<td><%=data.getParentLayoutId()%></td>
					<td><%=data.getName()%></td>
					<td><%=data.getTitle()%></td>
					<td><%=data.getDescription()%></td>
					<td><%=data.getKeywords()%></td>
					<td><%=data.getRobots()%></td>
					<td><%=data.getType()%></td>
					<td><%=data.getTypeSettings()%></td>
					<td><%=data.getHidden()%></td>
					<td><%=data.getFriendlyURL()%></td>
					<td><a href="/JSONServiceProject/delete/<%=data.getId()%>">
						<button>Eliminar</button>
						</a>
					</td>
				</tr>
				<%
					}
				%>
			</table>
		</div>
	</div>
	<div>
		<h1>Insertar Nuevo</h1>
		<form action="/JSONServiceProject/listado" method="post">
			<div>
				<label for="userId">userId:</label>
				<input type="text" id="userId" name="userId" />
			</div>
			<div>
				<label for="groupId">groupId:</label>
				<input type="text" id="groupId" name="groupId" />
			</div>
			<div>
				<label for="privateLayout">privateLayout:</label>
				<input type="text" id="privateLayout" name="privateLayout" />
			</div>
			<div>
				<label for="parentLayoutId">parentLayoutId:</label>
				<input type="text" id="parentLayoutId" name="parentLayoutId" />
			</div>
			<div>
				<label for="name">name:</label>
				<input type="text" id="name" name="name" />
			</div>
			<div>
				<label for="title">title:</label>
				<input type="text" id="title" name="title" />
			</div>
			<div>
				<label for="description">description:</label>
				<input type="text" id="description" name="description" />
			</div>
			<div>
				<label for="keywords">keywords:</label>
				<input type="text" id="keywords" name="keywords" />
			</div>
			<div>
				<label for="robots">robots:</label>
				<input type="text" id="robots" name="robots" />
			</div>
			<div>
				<label for="type">type:</label>
				<input type="text" id="type" name="type" />
			</div>
			<div>
				<label for="typeSettings">typeSettings:</label>
				<input type="text" id="typeSettings" name="typeSettings" />
			</div>
			<div>
				<label for="hidden">hidden:</label>
				<input type="text" id="hidden" name="hidden" />
			</div>
			<div>
				<label for="friendlyUrl">friendlyUrl:</label>
				<input type="text" id="friendlyUrl" name="friendlyUrl" />
			</div>
			<button type="submit">Enviar</button>
		</form>
	</div>
</div>
