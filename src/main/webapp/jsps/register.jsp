<%@ page language="java" contentType="application/json; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="org.json.*, edu.uclm.esi.web.Manager, edu.uclm.esi.games.model.*" %>
<%@page import="edu.uclm.esi.jsoner.JSONer"%>

<%
String p=request.getParameter("p");
JSONObject resultado=new JSONObject();
try {
	if (!request.getMethod().equals("POST"))
		throw new Exception("Método no soportado");
	JSONObject jso=new JSONObject(p);
	
	if (!jso.getString("type").equals("Register")) {
		resultado.put("type", "error");
		resultado.put("message", "Mensaje inesperado");
	} else {
		String email=jso.getString("email");
		String userName=jso.getString("userName");
		String pwd1=jso.getString("pwd1");
		String pwd2=jso.getString("pwd2");
		AbstractPlayer player=Manager.get().register(email, userName, pwd1, pwd2);
		JSONObject jsoRespuesta=JSONer.toJSON(player);
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
