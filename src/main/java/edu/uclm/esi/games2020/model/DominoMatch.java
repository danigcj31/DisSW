package edu.uclm.esi.games2020.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

public class DominoMatch extends Match {

	private TacoFichas tacoFichas;
	private List<FichaDomino> fichasMesa;
	private User jugadorConElTurno;
	private List<FichaDomino> tableroDomino;

	public DominoMatch() {
		super();
		this.tacoFichas = new TacoFichas();
		this.tacoFichas.suffle();
		this.fichasMesa = new ArrayList<>();
		this.tableroDomino = new ArrayList<>();
		for (int i = 0; i < 14; i++)
			this.fichasMesa.add(this.tacoFichas.getFichaDomino());

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
		JSONArray jsaMesa = new JSONArray();
		for (FichaDomino ficha : this.fichasMesa)
			jsaMesa.put(ficha);

		FichaDomino ficha1 = this.tacoFichas.getFichaDomino();
		FichaDomino ficha2 = this.tacoFichas.getFichaDomino();
		FichaDomino ficha3 = this.tacoFichas.getFichaDomino();
		FichaDomino ficha4 = this.tacoFichas.getFichaDomino();
		FichaDomino ficha5 = this.tacoFichas.getFichaDomino();
		FichaDomino ficha6 = this.tacoFichas.getFichaDomino();
		FichaDomino ficha7 = this.tacoFichas.getFichaDomino();

		ficha1.setState(player.getState());
		ficha2.setState(player.getState());
		ficha3.setState(player.getState());
		ficha4.setState(player.getState());
		ficha5.setState(player.getState());
		ficha6.setState(player.getState());
		ficha7.setState(player.getState());

		JSONArray jsaFichasDelJugador = new JSONArray();
		jsaFichasDelJugador.put(ficha1.toJSON());
		jsaFichasDelJugador.put(ficha2.toJSON());
		jsaFichasDelJugador.put(ficha3.toJSON());
		jsaFichasDelJugador.put(ficha4.toJSON());
		jsaFichasDelJugador.put(ficha5.toJSON());
		jsaFichasDelJugador.put(ficha6.toJSON());
		jsaFichasDelJugador.put(ficha7.toJSON());

		jso.put("data", jsaFichasDelJugador);
		jso.put("mesa", jsaMesa);

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
		for (int i = 0; i < this.tableroDomino.size(); i++) {
			jsa.put(this.tableroDomino.get(i).toJSON());
		}
		return jsa;
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
	protected boolean IsWinner(User player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean isDraw() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void notificarAClientes() throws IOException {
		JSONObject jsoMovimiento = new JSONObject();
		jsoMovimiento.put("type", "actualizacionTablero");
		jsoMovimiento.put("tablero", getTablero());
		jsoMovimiento.put("jugadorConElTurno", cambiarTurno().getUserName());
		jsoMovimiento.put("ganador", "");
		jsoMovimiento.put("empate", "F");
		/*
		 * for (User player : this.players) if (IsWinner(player)) {
		 * jsoMovimiento.put("ganador", player.getUserName()); } else if (isDraw())
		 * jsoMovimiento.put("empate", "T");
		 */
		for (User player : this.players) {
			player.send(jsoMovimiento);
		}

	}

	@Override
	protected void actualizarTablero(JSONObject jsoMovimiento, User usuario) {
		boolean primeraFicha = false; //PARA SABER SI SÓLO HAY UNA FICHA EN EL TABLERO
		if(!jsoMovimiento.getJSONObject("juntoA").getBoolean("ocupadoRight") && !jsoMovimiento.getJSONObject("juntoA").getBoolean("ocupadoLeft"))
			primeraFicha = true;

		// Primera ficha
		if (jsoMovimiento.getJSONObject("pongo").getInt("numberLeft") == jsoMovimiento.getJSONObject("juntoA")
				.getInt("numberLeft")
				&& jsoMovimiento.getJSONObject("pongo").getInt("numberRight") == jsoMovimiento.getJSONObject("juntoA")
						.getInt("numberRight")) {
			this.tableroDomino.add(new FichaDomino(jsoMovimiento.getJSONObject("pongo").getInt("numberLeft"),
					jsoMovimiento.getJSONObject("pongo").getInt("numberRight")));

		} else if (jsoMovimiento.getJSONObject("juntoA").getBoolean("ocupadoRight") || primeraFicha == true) { // PARA PONER FICHA AL PRINCIPIO
			// DERECHA-IZQUIERDA
			if (jsoMovimiento.getJSONObject("pongo").getInt("numberRight") == jsoMovimiento.getJSONObject("juntoA") // PONER
																													// NORMAL
					.getInt("numberLeft")) {
				this.tableroDomino.add(0, new FichaDomino(jsoMovimiento.getJSONObject("pongo").getInt("numberLeft"),
						jsoMovimiento.getJSONObject("pongo").getInt("numberRight")));

				// IZQUIERDA-IZQUIERDA (vuelta)
			} else if (jsoMovimiento.getJSONObject("pongo").getInt("numberLeft") == jsoMovimiento // PONER GIRAO
					.getJSONObject("juntoA").getInt("numberLeft")) {
				this.tableroDomino.add(0, new FichaDomino(jsoMovimiento.getJSONObject("pongo").getInt("numberRight"),
						jsoMovimiento.getJSONObject("pongo").getInt("numberLeft")));

			} primeraFicha = false;

		} else if (jsoMovimiento.getJSONObject("juntoA").getBoolean("ocupadoLeft") || primeraFicha == true) { // PARA PONER FICHA AL FINAL

			// IZQUIERDA-DERECHA
			if (jsoMovimiento.getJSONObject("pongo").getInt("numberLeft") == jsoMovimiento.getJSONObject("juntoA") // PONER
																													// NORMAL
					.getInt("numberRight")) {
				this.tableroDomino.add(new FichaDomino(jsoMovimiento.getJSONObject("pongo").getInt("numberLeft"),
						jsoMovimiento.getJSONObject("pongo").getInt("numberRight")));

			}
			// DERECHA-DERECHA (vuelta)
			else if (jsoMovimiento.getJSONObject("pongo").getInt("numberRight") == jsoMovimiento.getJSONObject("juntoA") // PONER
																															// GIRAO
					.getInt("numberRight")) {
				this.tableroDomino.add(new FichaDomino(jsoMovimiento.getJSONObject("pongo").getInt("numberRight"),
						jsoMovimiento.getJSONObject("pongo").getInt("numberLeft")));

			}
			primeraFicha = false;
		}

	}

	@Override
	protected void comprobarLegalidad(JSONObject jsoMovimiento, User usuario) throws Exception {

		if ((jsoMovimiento.getJSONObject("juntoA").getBoolean("ocupadoRight"))
				&& (jsoMovimiento.getJSONObject("juntoA").getBoolean("ocupadoLeft")))
			throw new Exception("Movimiento inválido");

	}

	@Override
	protected void comprobarTurno(User usuario) throws Exception {
		// TODO Auto-generated method stub

	}

}
