package edu.uclm.esi.games2020.model;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import edu.uclm.esi.games2020.dao.UserDAO;

@Component
public class Manager {
	private ConcurrentHashMap<String, User> connectedUsersByUserName;
	private ConcurrentHashMap<String, User> connectedUsersByHttpSession;
	private ConcurrentHashMap<String, Game> games;
	private ConcurrentHashMap<String, Match> pendingMatches;
	private ConcurrentHashMap<String, Match> inPlayMatches;

	@Autowired
	private UserDAO userDAO;

	private Manager() {
		this.connectedUsersByUserName = new ConcurrentHashMap<>();
		this.connectedUsersByHttpSession = new ConcurrentHashMap<>();
		this.games = new ConcurrentHashMap<>();
		this.pendingMatches = new ConcurrentHashMap<>();
		this.inPlayMatches = new ConcurrentHashMap<>();

		Game ter = new TresEnRayaGame();
		Game domino = new DominoGame();

		this.games.put(domino.getName(), domino);
		this.games.put(ter.getName(), ter);

	}

	public Match joinToMatch(User user, String gameName) {
		Game game = this.games.get(gameName);
		Match match = game.joinToMatch(user);
		if (!pendingMatches.containsKey(match.getId()))
			pendingMatches.put(match.getId(), match);
		return match;
	}

	private static class ManagerHolder {
		static Manager singleton = new Manager();
	}

	@Bean
	public static Manager get() {
		return ManagerHolder.singleton;
	}

	public User login(HttpSession httpSession, String userName, String pwd) throws Exception {
		try {
			User user = userDAO.findById(userName).get();
			if (user.getPwd().equals(pwd)) {
				user.setHttpSession(httpSession);
				this.connectedUsersByUserName.put(userName, user);
				this.connectedUsersByHttpSession.put(httpSession.getId(), user);
				return user;
			} else
				throw new Exception("Credenciales inválidas");
		} catch (SQLException e) {
			throw new Exception("Credenciales inválidas");
		}
	}

	public void register(String email, String userName, String pwd) throws Exception {
		User user = new User();
		user.setEmail(email);
		user.setUserName(userName);
		user.setPwd(pwd);
		userDAO.save(user);
	}

	public void logout(User user) {
		this.connectedUsersByUserName.remove(user.getUserName());
		this.connectedUsersByHttpSession.remove(user.getHttpSession().getId());
	}

	public JSONArray getGames() {
		Collection<Game> gamesList = this.games.values();
		JSONArray result = new JSONArray();
		for (Game game : gamesList)
			result.put(game.getName());
		return result;
	}

	public void playerReady(String idMatch, WebSocketSession session) throws IOException {
		Match match = this.pendingMatches.get(idMatch);
		match.playerReady(session);
		if (match.ready()) {
			this.pendingMatches.remove(idMatch);
			this.inPlayMatches.put(idMatch, match);
			match.notifyStart();
		}
	}

	public User findUserByHttpSessionId(String httpSessionId) {
		return this.connectedUsersByHttpSession.get(httpSessionId);
	}

	public void mover(String idMatch, User user, String posicion, String juntoA) throws Exception {
		if (juntoA.equals("")) {
			Match match = this.inPlayMatches.get(idMatch);
			match.mover(user, posicion,"");
		} else {
			Match match = this.inPlayMatches.get(idMatch);
			match.mover(user, posicion, juntoA);
		}
	}

}
