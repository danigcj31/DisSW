package edu.uclm.esi.games2020.model;

import edu.uclm.esi.games2020.dao.UserDAO;

public class User {
	private String userName;
	private String email;
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public static User identify(String userName, String pwd) throws Exception {
		return UserDAO.identify(userName, pwd);
	}
}
