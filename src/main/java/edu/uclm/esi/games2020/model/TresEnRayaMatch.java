package edu.uclm.esi.games2020.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;
import edu.uclm.esi.games2020.model.Match;

public class TresEnRayaMatch extends Match {

	private List<String> tablero;
	private User jugadorConElTurno;

	public TresEnRayaMatch() {
		super();

		this.tablero = new ArrayList<>();

		this.tablero.add("-");
		this.tablero.add("-");
		this.tablero.add("-");
		this.tablero.add("-");
		this.tablero.add("-");
		this.tablero.add("-");
		this.tablero.add("-");
		this.tablero.add("-");
		this.tablero.add("-");

	}

	public int getCurrentPlayer() {
		return 0;
	}

	@Override
	protected void setState(User user) {
		IState state = new TerState();
		user.setState(state);
		state.setUser(user);
	}

	@Override
	public void start() throws IOException {
		this.started = true;
		super.notifyStart();
	}

	@Override
	protected JSONObject startData(User player) {

		JSONObject jso = new JSONObject();
		JSONArray jsaTablero = new JSONArray();
		for (String ficha : this.tablero)
			jsaTablero.put(ficha);
		jso.put("tablero", jsaTablero);

		return jso;
	}

	private String getFicha() {
		if (this.jugadorConElTurno == this.players.get(0))
			return "X";
		return "O";
	}

	@Override
	protected User sortearTurno() {
		Random dado = new Random();
		if (dado.nextBoolean()) {
			this.jugadorConElTurno = players.get(0);
		} else {
			this.jugadorConElTurno = players.get(1);
		}
		return this.jugadorConElTurno;
	}

	@Override
	protected JSONArray getTablero() {
		// colocar las fichas en un JSONObject
		JSONArray jsa = new JSONArray();
		for (int i = 0; i < this.tablero.size(); i++) {
			jsa.put(this.tablero.get(i));
		}
		return jsa;
	}

	protected boolean IsWinner(User player) {
		Boolean winner = false;

		if (IsWinnerFilas(player))
			winner = true;
		else if (IsWinnerColumnas(player))
			winner = true;
		else if (IsWinnerDiagonales(player))
			winner = true;

		return winner;

	}

	private Boolean IsWinnerDiagonales(User player) {
		boolean IsWinner = false;
		if (player == this.players.get(0)) {
			if (this.tablero.get(0).equals("X") && this.tablero.get(4).equals("X") && this.tablero.get(8).equals("X"))
				IsWinner = true;
			if (this.tablero.get(6).equals("X") && this.tablero.get(4).equals("X") && this.tablero.get(2).equals("X"))
				IsWinner = true;

		} else {
			if (this.tablero.get(0).equals("O") && this.tablero.get(4).equals("O") && this.tablero.get(8).equals("O"))
				IsWinner = true;
			if (this.tablero.get(6).equals("O") && this.tablero.get(4).equals("O") && this.tablero.get(2).equals("O"))
				IsWinner = true;

		}

		return IsWinner;
	}

	private Boolean IsWinnerColumnas(User player) {
		boolean IsWinner = false;
		if (player == this.players.get(0)) {
			if (this.tablero.get(0).equals("X") && this.tablero.get(3).equals("X") && this.tablero.get(6).equals("X"))
				IsWinner = true;
			if (this.tablero.get(1).equals("X") && this.tablero.get(4).equals("X") && this.tablero.get(7).equals("X"))
				IsWinner = true;
			if (this.tablero.get(2).equals("X") && this.tablero.get(5).equals("X") && this.tablero.get(8).equals("X"))
				IsWinner = true;
		} else {
			if (this.tablero.get(0).equals("O") && this.tablero.get(3).equals("O") && this.tablero.get(6).equals("O"))
				IsWinner = true;
			if (this.tablero.get(1).equals("O") && this.tablero.get(4).equals("O") && this.tablero.get(7).equals("O"))
				IsWinner = true;
			if (this.tablero.get(2).equals("O") && this.tablero.get(5).equals("O") && this.tablero.get(8).equals("O"))
				IsWinner = true;
		}

		return IsWinner;
	}

	private Boolean IsWinnerFilas(User player) {
		boolean IsWinner = false;
		if (player == this.players.get(0)) {
			if (this.tablero.get(0).equals("X") && this.tablero.get(1).equals("X") && this.tablero.get(2).equals("X"))
				IsWinner = true;
			if (this.tablero.get(3).equals("X") && this.tablero.get(4).equals("X") && this.tablero.get(5).equals("X"))
				IsWinner = true;
			if (this.tablero.get(6).equals("X") && this.tablero.get(7).equals("X") && this.tablero.get(8).equals("X"))
				IsWinner = true;
		} else {
			if (this.tablero.get(0).equals("O") && this.tablero.get(1).equals("O") && this.tablero.get(2).equals("O"))
				IsWinner = true;
			if (this.tablero.get(3).equals("O") && this.tablero.get(4).equals("O") && this.tablero.get(5).equals("O"))
				IsWinner = true;
			if (this.tablero.get(6).equals("O") && this.tablero.get(7).equals("O") && this.tablero.get(8).equals("O"))
				IsWinner = true;
		}

		return IsWinner;
	}

	@Override
	protected User cambiarTurno() {
		if (this.jugadorConElTurno.equals(players.get(0)))
			this.jugadorConElTurno = players.get(1);
		else
			this.jugadorConElTurno = players.get(0);

		return this.jugadorConElTurno;
	}

	@Override
	protected boolean isDraw() {
		boolean isDraw = true;

		for (int i = 0; i < this.tablero.size(); i++) {
			if (this.tablero.get(i).equals("-"))
				isDraw = false;
		}
		return isDraw;
	}

	@Override
	protected void comprobarTurno(User usuario) throws Exception {
		if (this.jugadorConElTurno != usuario)
			throw new Exception("No tienes el turno");
	}

	@Override
	protected void comprobarLegalidad(JSONObject jsoMovimiento, User usuario) throws Exception {
		int posicion = jsoMovimiento.getInt("ficha");
		if (!this.tablero.get(posicion).equals("-"))
			throw new Exception("Casilla ocupada");
	}

	@Override
	protected void notificarAClientes() throws IOException {
		for (User player : this.players) {
			player.send(jsoMovimiento);
		}

	}

	@Override
	protected void actualizarTablero(JSONObject jsoMovimiento, User usuario) {

		this.tablero.set(jsoMovimiento.getInt("ficha"), getFicha());
		JSONObject jso = new JSONObject();
		jso.put("type", "actualizacionTablero");
		jso.put("tablero", getTablero());
		jso.put("jugadorConElTurno", cambiarTurno().getUserName());
		jso.put("ganador", "");
		jso.put("empate", "F");
		for (User player : this.players)
			if (IsWinner(player)) {
				jso.put("ganador", player.getUserName());
			} else if (isDraw())
				jso.put("empate", "T");

	}

	@Override
	protected void robar(JSONObject jsoMovimiento) {
		// TODO Auto-generated method stub
		
	}

}
