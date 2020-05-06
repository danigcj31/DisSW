package edu.uclm.esi.games2020.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.uclm.esi.games2020.exceptions.CasillaOcupadaException;
import edu.uclm.esi.games2020.exceptions.NoTienesElTurnoException;

public class TresEnRayaMatch extends Match {

	private List<String> tablero;
	private User jugadorConElTurno;
	private Random dado = new Random();

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

	protected boolean isWinner(User player, JSONObject jsoMovimiento) {
		boolean winner = false;

		if (isWinnerFilas(player) || isWinnerColumnas(player) || isWinnerDiagonales(player)) {
			winner = true;
		}

		return winner;

	}

	private Boolean isWinnerDiagonales(User player) {
		boolean isWinner = false;
		if (player == this.players.get(0)) {
			isWinner = comprobarDiagonalIzqDer("X");
			if (!isWinner) {
				isWinner = comprobarDiagonalDerIzq("X");
			}

		} else {
			isWinner = comprobarDiagonalIzqDer("O");
			if (!isWinner) {
				isWinner = comprobarDiagonalDerIzq("O");
			}

		}

		return isWinner;
	}

	private boolean comprobarDiagonalDerIzq(String ficha) {
		boolean isWinner = false;
		if (this.tablero.get(6).equals(ficha) && this.tablero.get(4).equals(ficha) && this.tablero.get(2).equals(ficha))
			isWinner = true;
		return isWinner;
	}

	private boolean comprobarDiagonalIzqDer(String ficha) {
		boolean isWinner = false;
		if (this.tablero.get(0).equals(ficha) && this.tablero.get(4).equals(ficha) && this.tablero.get(8).equals(ficha))
			isWinner = true;
		return isWinner;
	}

	private Boolean isWinnerColumnas(User player) {
		boolean isWinner = false;
		if (player == this.players.get(0)) {

			isWinner = comprobarPrimeraColumna("X");
			if (!isWinner) {
				isWinner = comprobarSegundaColumna("X");
			}
			if (!isWinner) {
				isWinner = comprobarTerceraColumna("X");
			}
		} else {
			isWinner = comprobarPrimeraColumna("O");
			if (!isWinner) {
				isWinner = comprobarSegundaColumna("O");
			}
			if (!isWinner) {
				isWinner = comprobarTerceraColumna("O");
			}
		}

		return isWinner;
	}

	private boolean comprobarTerceraColumna(String ficha) {
		boolean isWinner = false;
		if (this.tablero.get(2).equals(ficha) && this.tablero.get(5).equals(ficha) && this.tablero.get(8).equals(ficha))
			isWinner = true;
		return isWinner;
	}

	private boolean comprobarSegundaColumna(String ficha) {
		boolean isWinner = false;
		if (this.tablero.get(1).equals(ficha) && this.tablero.get(4).equals(ficha) && this.tablero.get(7).equals(ficha))
			isWinner = true;
		return isWinner;
	}

	private boolean comprobarPrimeraColumna(String ficha) {
		boolean isWinner = false;
		if (this.tablero.get(0).equals(ficha) && this.tablero.get(3).equals(ficha) && this.tablero.get(6).equals(ficha))
			isWinner = true;
		return isWinner;
	}

	private boolean comprobarSegundaFila(String ficha) {
		boolean isWinner = false;
		if (this.tablero.get(3).equals(ficha) && this.tablero.get(4).equals(ficha) && this.tablero.get(5).equals(ficha))
			isWinner = true;
		return isWinner;
	}

	private boolean comprobarTerceraFila(String ficha) {
		boolean isWinner = false;
		if (this.tablero.get(6).equals(ficha) && this.tablero.get(7).equals(ficha) && this.tablero.get(8).equals(ficha))
			isWinner = true;
		return isWinner;
	}

	private boolean comprobarPrimeraFila(String ficha) {
		boolean isWinner = false;
		if (this.tablero.get(0).equals(ficha) && this.tablero.get(1).equals(ficha) && this.tablero.get(2).equals(ficha))
			isWinner = true;
		return isWinner;
	}

	private Boolean isWinnerFilas(User player) {
		boolean isWinner = false;
		if (player == this.players.get(0)) {
			isWinner = comprobarPrimeraFila("X");
			if (!isWinner) {
				isWinner = comprobarSegundaFila("X");
			}
			if (!isWinner) {
				isWinner = comprobarTerceraFila("X");
			}

		} else {
			isWinner = comprobarPrimeraFila("O");
			if (!isWinner) {
				isWinner = comprobarSegundaFila("O");
			}
			if (!isWinner) {
				isWinner = comprobarTerceraFila("O");
			}

		}

		return isWinner;
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
			throw new NoTienesElTurnoException();
	}

	@Override
	protected void comprobarLegalidad(JSONObject jsoMovimiento, User usuario) throws Exception {
		int posicion = jsoMovimiento.getInt("ficha");
		if (!this.tablero.get(posicion).equals("-"))
			throw new CasillaOcupadaException();
	}

	@Override
	protected void notificarAClientes(JSONObject jsoMovimiento) throws IOException {
		JSONObject jso = new JSONObject();
		jso.put("type", "actualizacionTablero");
		jso.put("tablero", getTablero());
		jso.put("jugadorConElTurno", this.jugadorConElTurno.getUserName());
		jso.put("ganador", "");
		jso.put("empate", "F");
		for (User player : this.players)
			if (isWinner(player, jsoMovimiento)) {
				jso.put("ganador", player.getUserName());
			} else if (isDraw()) {
				jso.put("empate", "T");
			}

		for (User player : this.players) {
			player.send(jso);
		}

	}

	@Override
	protected void actualizarTablero(JSONObject jsoMovimiento, User usuario) {

		this.tablero.set(jsoMovimiento.getInt("ficha"), getFicha());

	}

	@Override
	protected void robar(JSONObject jsoMovimiento, User usuario) {
		//En este juego no se roba
	}

}
