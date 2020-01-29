<%@ page language="java" contentType="application/json; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="org.json.*, edu.uclm.esi.web.Manager" %>

<%
String p=request.getParameter("p");
JSONObject resultado=new JSONObject();
try {
	if (!request.getMethod().equals("GET"))
		throw new Exception("Método no soportado");
	JSONObject jso=new JSONObject(p);
	if (!jso.getString("type").equals("GetGames")) {
		resultado.put("type", "error");
		resultado.put("message", "Mensaje inesperado");
	} else {
		JSONObject jsoRespuesta=Manager.get().getGames();
		resultado.put("resultado", jsoRespuesta);
		resultado.put("type", "OK");
	}
}
catch (Exception e) {
	resultado.put("type", "error");
	resultado.put("message", e.getMessage());
}
%>

<%= resultado.toString() %>
