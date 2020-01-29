package edu.uclm.esi.games2020.model;

import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import edu.uclm.esi.games2020.dao.UserDAO;
import edu.uclm.esi.games2020.model.User;

public class Manager {
	
	private ConcurrentHashMap<String, User> connectedUsers;
	
	private Manager() {
		this.connectedUsers = new ConcurrentHashMap<>();
	}
	
	private static class ManagerHolder {
		static Manager singleton=new Manager();
	}
	
	public static Manager get() {
		return ManagerHolder.singleton;
	}

	public User login(String userName, String pwd) throws Exception {
		try {
			User user = UserDAO.identify(userName, pwd);
			this.connectedUsers.put(userName, user);
			return user;
		}
		catch(SQLException e) {
			throw new Exception("Credenciales inválidas");
		}
	}
	
	public void register(String email, String userName, String pwd) throws Exception {
		UserDAO.insert(email, userName, pwd);
	}
	
	public void logout(User user) {
		this.connectedUsers.remove(user.getUserName());
	}
}
