package edu.uclm.esi.games2020.http;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.games2020.model.Manager;
import edu.uclm.esi.games2020.model.Match;
import edu.uclm.esi.games2020.model.User;

@RestController
public class Controller {
	private static final String MATCH = "match";
	@PostMapping("/login")
	public void login(HttpSession session, @RequestBody Map<String, Object> credenciales) throws Exception {
		JSONObject jso = new JSONObject(credenciales);
		String userName = jso.getString("userName");
		String pwd = jso.getString("pwd");
		User user = Manager.get().login(session, userName, pwd);
		session.setAttribute("user", user);
	}

	@PostMapping("/register")
	public void register(@RequestBody Map<String, Object> credenciales) throws Exception {
		JSONObject jso = new JSONObject(credenciales);
		String userName = jso.getString("userName");
		String email = jso.getString("email");
		String pwd1 = jso.getString("pwd1");
		String pwd2 = jso.getString("pwd2");
		String creditCard = jso.getString("creditCard");
		if (pwd1.equals(pwd2))
			Manager.get().register(email, userName, pwd1, creditCard);
	}

	@GetMapping("/getGames")
	public JSONArray getGames(HttpSession session) throws Exception{
		return Manager.get().getGames();
	}

	@PostMapping(value = "/joinToMatchConMap", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> joinToMatchConMap(HttpSession session, HttpServletResponse response,
			@RequestBody Map<String, Object> info) {
		User user = (User) session.getAttribute("user");
		if (user == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Identíficate antes de jugar");
		} else {
			JSONObject jso = new JSONObject(info);
			String game = jso.getString("game");
			Match match = Manager.get().joinToMatch(user, game);
			HashMap<String, Object> resultado = new HashMap<>();
			resultado.put("type", MATCH);
			resultado.put(MATCH, match);
			return resultado;
		}
	}

	@PostMapping(value = "/joinToMatch", produces = MediaType.APPLICATION_JSON_VALUE)
	public String joinToMatch(HttpSession session, HttpServletResponse response,
			@RequestBody Map<String, Object> info) {
		User user = (User) session.getAttribute("user");
		if (user == null) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Identíficate antes de jugar");
		} else {
			JSONObject jso = new JSONObject(info);
			String game = jso.getString("game");
			Match match = Manager.get().joinToMatch(user, game);
			JSONObject resultado = new JSONObject();
			resultado.put("type", MATCH);
			resultado.put(MATCH, match.toJSON());
			resultado.put("juego", game);
			return resultado.toString();
		}
	}
}
