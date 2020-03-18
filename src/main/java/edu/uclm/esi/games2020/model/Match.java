package edu.uclm.esi.games2020.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.socket.WebSocketSession;

public abstract class Match {
	protected List<User> players;
	protected String id;
	protected boolean started;
	private int readyPlayers;
	private Game game;

	public Match() {
		this.id = UUID.randomUUID().toString();
		this.players = new ArrayList<>();
	}

	public void addPlayer(User user) {
		this.players.add(user);
		setState(user);
	}

	protected abstract void setState(User user);

	public List<User> getPlayers() {
		return players;
	}

	public String getId() {
		return id;
	}

	public abstract void start() throws IOException;

	public JSONObject toJSON() {
		JSONObject jso = new JSONObject();
		jso.put("idMatch", this.id);
		jso.put("started", this.started);
		JSONArray jsa = new JSONArray();
		for (User user : this.players)
			jsa.put(user.toJSON());
		jso.put("players", jsa);
		return jso;
	}

	public void notifyStart() throws IOException {
		
		JSONObject jso = this.toJSON();
		jso.put("type", "matchStarted");
		jso.put("jugadorConElTurno", sortearTurno().getUserName());
		for (User player : this.players) {

			jso.put("startData", startData(player));
			player.send(jso);
		}
	}

	protected void actualizarTableros() throws IOException {
		JSONObject jso = new JSONObject();
		jso.put("type", "actualizacionTablero");
		jso.put("tablero",this.getTablero());
		jso.put("ganador", "");
		for (User player : this.players)
		if(IsWinner(player)) {
			//jso.ganador = player.getUserName();
			jso.put("ganador", player.getUserName());
		}

		for (User player : this.players) {
			player.send(jso);
		}
		
	}

	protected abstract boolean IsWinner(User player);

	protected abstract JSONArray getTablero();

	protected abstract User sortearTurno();

	protected abstract JSONObject startData(User player);

	public void playerReady(WebSocketSession session) {
		++readyPlayers;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public boolean ready() {
		return this.readyPlayers == game.requiredPlayers;
	}

	protected abstract void mover(User user, String movimiento) throws Exception;
}
