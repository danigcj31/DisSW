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

	@Override
	protected User cambiarTurno() {
		if (this.jugadorConElTurno.equals(players.get(0)))
			this.jugadorConElTurno = players.get(1);
		else
			this.jugadorConElTurno = players.get(0);

		return this.jugadorConElTurno;
	}

	@Override
	protected void comprobarLegalidad(JSONObject jsoMovimiento, User usuario) throws Exception {
		if ((jsoMovimiento.getJSONObject("juntoA").getBoolean("ocupadoRight"))
				&& (jsoMovimiento.getJSONObject("juntoA").getBoolean("ocupadoLeft")))
			throw new Exception("Movimiento inválido");
	}

	@Override
	protected void comprobarTurno(User usuario) throws Exception {
		if (this.jugadorConElTurno != usuario)
			throw new Exception("No tienes el turno");
	}

	public int getCurrentPlayer() {
		return 0;
	}

	private JSONObject getFichaRobada() {
		DominoState stateplayers = (DominoState) this.jugadorConElTurno.getState();
		return stateplayers.getUltFicha().toJSON();
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

	protected JSONArray getTacoFichas() {
		// colocar las fichas en un JSONObject
		JSONArray jsa = new JSONArray();
		for (int i = 0; i < this.fichasMesa.size(); i++) {
			jsa.put(this.fichasMesa.get(i).toJSON());
		}
		return jsa;
	}

	@Override
	protected boolean isDraw() {
		return false;
	}

	@Override
	protected boolean IsWinner(User player, JSONObject jsoMovimiento) {
		boolean isWinner = false;
		DominoState state0 = (DominoState) players.get(0).getState();
		DominoState state1 = (DominoState) players.get(1).getState();
		DominoState stateplayers = (DominoState) player.getState();
		if (this.jugadorConElTurno != player) {
			if (jsoMovimiento.getInt("nFichasJugador") - 1 == 0) {
				isWinner = true;

			} else if (jsoMovimiento.getInt("nFichasJugador") > 0 && jsoMovimiento.getJSONArray("taco").length() == 0) { // DESEMPATE
				isWinner = desempate(jsoMovimiento, state0, state1, stateplayers);

			}
		}
		return isWinner;
	}

	private boolean desempate(JSONObject jsoMovimiento, DominoState state0, DominoState state1,
			DominoState stateplayers) {
		boolean isWinner = false;
		if (jsoMovimiento.getBoolean("pasarTurno"))
			stateplayers.setPasarTurnoEmpate(true);
		if (state0.getPasarTurnoEmpate() && state1.getPasarTurnoEmpate()) {
			if (state0.getNumFichas() > state1.getNumFichas()) {
				if (state1 == stateplayers) {
					isWinner = true;
				}
			} else if (state0.getNumFichas() < state1.getNumFichas()) {
				if (state0 == stateplayers) {
					isWinner = true;
				}
			} else {
				isWinner = true;
				// En caso de empate a numero de fichas, el ganador es el segundo
			}
		}
		return isWinner;
	}

	@Override
	protected void notificarAClientes(JSONObject jsoMovimiento) throws IOException {
		JSONObject jso = new JSONObject();
		jso.put("type", "actualizacionTablero");
		jso.put("tablero", getTablero());
		jso.put("jugadorConElTurno", this.jugadorConElTurno.getUserName());
		jso.put("ganador", "");
		jso.put("empate", "F");
		jso.put("tacoFicha", getTacoFichas());
		if (jsoMovimiento.getBoolean("robar"))
			jso.put("fichaRobada", getFichaRobada());

		for (User player : this.players)
			if (IsWinner(player, jsoMovimiento))
				jso.put("ganador", player.getUserName());

		for (User player : this.players) {
			player.send(jso);
		}

	}

	protected void robar(JSONObject jsoMovimiento, User usuario) {
		DominoState stateplayers = (DominoState) usuario.getState();
		stateplayers.addFichas(this.fichasMesa.get(this.fichasMesa.size() - 1));
		this.fichasMesa.remove(this.fichasMesa.size() - 1);
	}

	@Override
	protected void setState(User user) {
		IState state = new DominoState();
		user.setState(state);
		state.setUser(user);
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
	public void start() throws IOException {
		this.started = true;
		super.notifyStart();
	}

	@Override
	protected JSONObject startData(User player) {

		JSONObject jso = new JSONObject();
		JSONArray jsaMesa = new JSONArray();
		for (FichaDomino ficha : this.fichasMesa)
			jsaMesa.put(ficha.toJSON());

		FichaDomino ficha1 = this.tacoFichas.getFichaDomino();
		FichaDomino ficha2 = this.tacoFichas.getFichaDomino();
		FichaDomino ficha3 = this.tacoFichas.getFichaDomino();
		FichaDomino ficha4 = this.tacoFichas.getFichaDomino();
		FichaDomino ficha5 = this.tacoFichas.getFichaDomino();
		FichaDomino ficha6 = this.tacoFichas.getFichaDomino();
		FichaDomino ficha7 = this.tacoFichas.getFichaDomino();

		DominoState state = (DominoState) player.getState();
		ficha1.setState(state);
		ficha2.setState(state);
		ficha3.setState(state);
		ficha4.setState(state);
		ficha5.setState(state);
		ficha6.setState(state);
		ficha7.setState(state);

		state.addFichas(ficha1, ficha2, ficha3, ficha4, ficha5, ficha6, ficha7);

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

	@Override
	protected void actualizarTablero(JSONObject jsoMovimiento, User usuario) throws Exception {
		boolean soloUnaFicha = false; // PARA SABER SI EL TABLERO ESTÁ VACÍO
		boolean fichaPuesta = false;
		boolean primeraFicha = false;
		DominoState stateplayers = (DominoState) usuario.getState();

		primeraFicha = AñadirPrimeraFicha(jsoMovimiento, stateplayers);

		if (!primeraFicha) {
			soloUnaFicha = comprobarSiSoloHayUnaFicha(jsoMovimiento);
		}
		fichaPuesta = posibilidadIzq(jsoMovimiento, stateplayers, soloUnaFicha);
		if (!fichaPuesta) {
			fichaPuesta = posibilidadDer(jsoMovimiento, stateplayers, soloUnaFicha);
		}

		if (!fichaPuesta && !primeraFicha)
			throw new Exception("Movimiento invalido");
	}

	private boolean posibilidadDer(JSONObject jsoMovimiento, DominoState stateplayers, boolean soloUnaFicha) {
		if (jsoMovimiento.getJSONObject("juntoA").getBoolean("ocupadoLeft") || soloUnaFicha) {
			// IZQUIERDA-DERECHA
			if (jsoMovimiento.getJSONObject("pongo").getInt("numberLeft") == jsoMovimiento.getJSONObject("juntoA")
					.getInt("numberRight")) {
				this.tableroDomino.add(new FichaDomino(jsoMovimiento.getJSONObject("pongo").getInt("numberLeft"),
						jsoMovimiento.getJSONObject("pongo").getInt("numberRight")));
				stateplayers.removeFichas(new FichaDomino(jsoMovimiento.getJSONObject("pongo").getInt("numberLeft"),
						jsoMovimiento.getJSONObject("pongo").getInt("numberRight")));
				return true;

			}
			// DERECHA-DERECHA (vuelta)
			else if (jsoMovimiento.getJSONObject("pongo").getInt("numberRight") == jsoMovimiento.getJSONObject("juntoA")
					.getInt("numberRight")) {
				this.tableroDomino.add(new FichaDomino(jsoMovimiento.getJSONObject("pongo").getInt("numberRight"),
						jsoMovimiento.getJSONObject("pongo").getInt("numberLeft")));
				stateplayers.removeFichas(new FichaDomino(jsoMovimiento.getJSONObject("pongo").getInt("numberLeft"),
						jsoMovimiento.getJSONObject("pongo").getInt("numberRight")));
				return true;

			}
		}
		return false;

	}

	private boolean posibilidadIzq(JSONObject jsoMovimiento, DominoState stateplayers, boolean soloUnaFicha) {
		if (jsoMovimiento.getJSONObject("juntoA").getBoolean("ocupadoRight") || soloUnaFicha) { // PARA
			// DERECHA-IZQUIERDA
			if (jsoMovimiento.getJSONObject("pongo").getInt("numberRight") == jsoMovimiento.getJSONObject("juntoA")
					.getInt("numberLeft")) {
				this.tableroDomino.add(0, new FichaDomino(jsoMovimiento.getJSONObject("pongo").getInt("numberLeft"),
						jsoMovimiento.getJSONObject("pongo").getInt("numberRight")));
				stateplayers.removeFichas(new FichaDomino(jsoMovimiento.getJSONObject("pongo").getInt("numberLeft"),
						jsoMovimiento.getJSONObject("pongo").getInt("numberRight")));
				return true;

				// IZQUIERDA-IZQUIERDA (vuelta)
			} else if (jsoMovimiento.getJSONObject("pongo").getInt("numberLeft") == jsoMovimiento
					.getJSONObject("juntoA").getInt("numberLeft")) {
				this.tableroDomino.add(0, new FichaDomino(jsoMovimiento.getJSONObject("pongo").getInt("numberRight"),
						jsoMovimiento.getJSONObject("pongo").getInt("numberLeft")));
				stateplayers.removeFichas(new FichaDomino(jsoMovimiento.getJSONObject("pongo").getInt("numberRight"),
						jsoMovimiento.getJSONObject("pongo").getInt("numberLeft")));
				return true;

			}

		}
		return false;

	}

	private boolean AñadirPrimeraFicha(JSONObject jsoMovimiento, DominoState stateplayers) {
		if (jsoMovimiento.getJSONObject("pongo").getInt("numberLeft") == jsoMovimiento.getJSONObject("juntoA")
				.getInt("numberLeft")
				&& jsoMovimiento.getJSONObject("pongo").getInt("numberRight") == jsoMovimiento.getJSONObject("juntoA")
						.getInt("numberRight")) {
			this.tableroDomino.add(new FichaDomino(jsoMovimiento.getJSONObject("pongo").getInt("numberLeft"),
					jsoMovimiento.getJSONObject("pongo").getInt("numberRight")));
			stateplayers.removeFichas(new FichaDomino(jsoMovimiento.getJSONObject("pongo").getInt("numberLeft"),
					jsoMovimiento.getJSONObject("pongo").getInt("numberRight")));
			return true;
		}
		return false;

	}

	private boolean comprobarSiSoloHayUnaFicha(JSONObject jsoMovimiento) {
		if (!jsoMovimiento.getJSONObject("juntoA").getBoolean("ocupadoRight")
				&& !jsoMovimiento.getJSONObject("juntoA").getBoolean("ocupadoLeft")) {
			return true;
		} else {
			return false;
		}
	}

}
