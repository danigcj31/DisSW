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
	private User user;
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

	protected boolean tieneElTurno(User user) {
		return (this.getCurrentPlayer() == 0 && user == this.user)
				|| (this.getCurrentPlayer() == 1 && user == this.user);
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

	@Override
	protected void mover(User user, String movimiento) throws Exception {
		if (user != this.jugadorConElTurno)
			throw new Exception("No tienes el turno");
		int posicion = Integer.parseInt(movimiento);
		if (!this.tablero.get(posicion).equals("-"))
			throw new Exception("Casilla ocupada");
		this.tablero.set(posicion, getFicha());
		//actualizar los tableros a los clientes
		this.actualizarTableros();
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
		//colocar las fichas en un JSONObject
		JSONArray jsa = new JSONArray();
		for (int i=0;i<this.tablero.size();i++) {
			jsa.put(this.tablero.get(i));
		}
		return jsa;
	}
}
