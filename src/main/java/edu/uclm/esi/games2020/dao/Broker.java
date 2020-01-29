package edu.uclm.esi.games2020.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Broker {
	private String url, user, pwd, databaseName;
	
	private Broker() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			url="jdbc:mysql://" + this.url + ":3306/" + this.databaseName + "?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8&allowMultiQueries=true&useSSL=false";
			user="ideas";
			pwd="ideas123";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	private static class BrokerHolder {
		static Broker singleton=new Broker();
	}
	
	public static Broker get() {
		return BrokerHolder.singleton;
	}

	public Connection getBd() throws SQLException {
		return DriverManager.getConnection(url, user, pwd);
	}
}
