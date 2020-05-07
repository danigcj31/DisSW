package edu.uclm.esi.games2020.model;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import edu.uclm.esi.games2020.dao.UserDAO;
import edu.uclm.esi.games2020.exceptions.CredencialesInvalidasException;

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

	public User findUserByHttpSessionId(String httpSessionId) {
		return this.connectedUsersByHttpSession.get(httpSessionId);
	}
	
	@Bean
	public static Manager get() {
		return ManagerHolder.singleton;
	}

	public JSONArray getGames() {
		Collection<Game> gamesList = this.games.values();
		JSONArray result = new JSONArray();
		for (Game game : gamesList)
			result.put(game.getName());
		return result;
	}
	
	public Match joinToMatch(User user, String gameName) {
		Game game = this.games.get(gameName);
		Match match = game.joinToMatch(user);
		if (!pendingMatches.containsKey(match.getId()))
			pendingMatches.put(match.getId(), match);
		return match;
	}
	
	public User login(HttpSession httpSession, String userName, String pwd) throws Exception {
		Optional<User> optUser = userDAO.findById(userName);
		if (optUser.isPresent()) {
			User user = optUser.get();
			String pwdEncrypted = user.getPwd();
			String pwdUsuario = encriptarMD5(pwd);
			if (pwdEncrypted.equals(pwdUsuario)) {
				user.setHttpSession(httpSession);
				this.connectedUsersByUserName.put(userName, user);
				this.connectedUsersByHttpSession.put(httpSession.getId(), user);
				return user;
			} else {
				throw new CredencialesInvalidasException();
			}
		} else throw new CredencialesInvalidasException();
	}
	
	public void logout(User user) {
		this.connectedUsersByUserName.remove(user.getUserName());
		this.connectedUsersByHttpSession.remove(user.getHttpSession().getId());
	}
	
	private static class ManagerHolder {
		static Manager singleton = new Manager();
	}
	
	public void mover(JSONObject jsoMovimiento, User usuario) throws Exception {
		Match match = this.inPlayMatches.get(jsoMovimiento.getString("idMatch"));
		match.mover(jsoMovimiento, usuario);
		
	}
	
	public void playerReady(String idMatch) throws IOException {
		Match match = this.pendingMatches.get(idMatch);
		match.playerReady();
		if (match.ready()) {
			this.pendingMatches.remove(idMatch);
			this.inPlayMatches.put(idMatch, match);
			match.notifyStart();
		}
	}

	public void register(String email, String userName, String pwd, String creditCard){
		User user = new User();
		user.setEmail(email);
		user.setUserName(userName);
		user.setPwd(encriptarMD5(pwd));
		
		user.setCreditCard(encriptarMD5(creditCard));
		userDAO.save(user);
	}
	
	private String encriptarMD5(String input){
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger number = new BigInteger(1, messageDigest);
			String hashtext = number.toString(16);

			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
			}
		catch (NoSuchAlgorithmException e) {
			 throw new RuntimeException(e);
		}
	}
}