package edu.uclm.esi.games2020.ws;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import edu.uclm.esi.games2020.model.Manager;
import edu.uclm.esi.games2020.model.User;

@Component
public class SpringWebSocket extends TextWebSocketHandler {
	private ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		System.out.println("Se ha conectado " + session.getId());
		HttpHeaders headers = session.getHandshakeHeaders();
		List<String> cookies = headers.get("cookie");
		for (String cookie : cookies)
			if (cookie.startsWith("JSESSIONID=")) {
				String httpSessionId = cookie.substring("JSESSIONID=".length());
				User user = Manager.get().findUserByHttpSessionId(httpSessionId);
				user.setSession(session);
				users.put(session.getId(), user);
				break;
			}
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		JSONObject jso = new JSONObject(message.getPayload().toString());
		if (jso.getString("type").equals("ready")) {
			Manager.get().playerReady(jso.getString("idMatch"), session);
		} else if (jso.getString("type").equals("movimientoT")) {
			User usuario = users.get(session.getId());
			try {
				Manager.get().mover(jso.getString("idMatch"), usuario, jso.getString("ficha"),null,null);
			} catch (Exception e) {
				usuario.send(new JSONObject().put("type", "error").put("error", e.getMessage()));

			}
		}else if (jso.getString("type").equals("movimientoD")) {
			User usuario = users.get(session.getId());
			try {
				Manager.get().mover(jso.getString("idMatch"), usuario,"", jso.getJSONObject("pongo"),jso.getJSONObject("juntoA"));
			} catch (Exception e) {
				usuario.send(new JSONObject().put("type", "error").put("error", e.getMessage()));

			}
		}
	}

}
