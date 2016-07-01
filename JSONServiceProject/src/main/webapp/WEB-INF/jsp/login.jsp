<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<h1>Login</h1>
<form method="post">
	<div>${message}</div>
	<div>
		<label for="pass">Contraseña</label>
		<input id="pass" name="pass" type="password" />
		<button type="submit">Enviar</button>
	</div>
</form>