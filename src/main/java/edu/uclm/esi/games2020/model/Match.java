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

	public List<User> getPlayers() {
		return players;
	}

	public String getId() {
		return id;
	}

	protected void mover(JSONObject jsoMovimiento, User usuario) throws Exception {
        comprobarTurno(usuario);
        if (!jsoMovimiento.getBoolean("robar") && !jsoMovimiento.getBoolean("pasarTurno")) {
            comprobarLegalidad(jsoMovimiento, usuario);
            actualizarTablero(jsoMovimiento, usuario);
            cambiarTurno();
            notificarAClientes(jsoMovimiento);
        } else if(jsoMovimiento.getBoolean("robar")){
            robar(jsoMovimiento,usuario);
            notificarAClientes(jsoMovimiento);
        }
        else if(jsoMovimiento.getBoolean("pasarTurno")) {
            cambiarTurno();
            notificarAClientes(jsoMovimiento);
        }
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

	public void playerReady(WebSocketSession session) {
		++readyPlayers;
	}

	public boolean ready() {
		return this.readyPlayers == game.requiredPlayers;
	}
	
	public void setGame(Game game) {
		this.game = game;
	}
	
	protected abstract void actualizarTablero(JSONObject jsoMovimiento, User usuario);

	protected abstract User cambiarTurno();

	protected abstract void comprobarLegalidad(JSONObject jsoMovimiento, User usuario) throws Exception;

	protected abstract void comprobarTurno(User usuario) throws Exception;

	protected abstract JSONArray getTablero();

	protected abstract boolean isDraw();
	
	protected abstract boolean IsWinner(User player,JSONObject jsoMovimiento);
	
	protected abstract void notificarAClientes(JSONObject jsoMovimiento) throws IOException;

	protected abstract void robar(JSONObject jsoMovimiento, User usuario);

	protected abstract void setState(User user);
	
	protected abstract User sortearTurno();

	public abstract void start() throws IOException;
	
	protected abstract JSONObject startData(User player);	
}