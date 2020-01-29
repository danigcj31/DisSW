<%@ page language="java" contentType="application/json; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="org.json.*, edu.uclm.esi.games2020.model.Manager, edu.uclm.esi.games2020.model.User" %>

<%
String p=request.getParameter("p");
JSONObject resultado=new JSONObject();
try {
	if (!request.getMethod().equals("POST"))
		throw new Exception("Método no soportado");
	
	String userName=request.getParameter("userName");
	String pwd=request.getParameter("pwd");
	User user=Manager.get().login(userName, pwd);
	session.setAttribute("user", user);
	resultado.put("type", "OK");
}
catch (Exception e) {
	resultado.put("type", "error");
	resultado.put("message", e.getMessage());
}
%>

<%= resultado.toString() %>
