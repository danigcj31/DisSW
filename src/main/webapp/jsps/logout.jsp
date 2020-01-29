<%@ page language="java" contentType="application/json; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="org.json.*, edu.uclm.esi.web.Manager, edu.uclm.esi.games.model.*" %>

<%
JSONObject resultado=new JSONObject();
try {
	if (!request.getMethod().equals("GET"))
		throw new Exception("Método no soportado");

	AbstractPlayer player=(AbstractPlayer) session.getAttribute("player");
	JSONObject jsoRespuesta=Manager.get().logout(player);
	session.invalidate();
	resultado.put("resultado", jsoRespuesta);
	resultado.put("tipo", "OK");
}
catch (Exception e) {
	resultado.put("tipo", "error");
	resultado.put("message", e.getMessage());
}
%>

<%= resultado.toString() %>

<%= resultado.toString() %>
