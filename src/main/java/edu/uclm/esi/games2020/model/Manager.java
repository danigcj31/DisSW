package edu.uclm.esi.games2020.model;

import java.sql.SQLException;

import edu.uclm.esi.games2020.dao.UserDAO;
import edu.uclm.esi.games2020.model.User;

public class Manager {
	
	private Manager() {
	}
	
	private static class ManagerHolder {
		static Manager singleton=new Manager();
	}
	
	public static Manager get() {
		return ManagerHolder.singleton;
	}

	public User login(String userName, String pwd) throws Exception {
		try {
			return UserDAO.identify(userName, pwd);			
		}
		catch(SQLException e) {
			throw new Exception("Credenciales inválidas");
		}
	}
}
