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

	@Override
	protected void mover(User user, String posicion, JSONObject pongo, JSONObject juntoA) throws Exception {
		boolean valido = false;

		// Primera ficha
		if (pongo.getInt("numberLeft") == juntoA.getInt("numberLeft")
				&& pongo.getInt("numberRight") == juntoA.getInt("numberRight")) {
			this.tableroDomino.add(new FichaDomino(pongo.getInt("numberLeft"), pongo.getInt("numberRight")));
			valido = true;
		}
		else if (juntoA.getString("ocupadoRight").equals("true")) {
			// DERECHA-IZQUIERDA
			if (pongo.getInt("numberRight") == juntoA.getInt("numberLeft")) {
				this.tableroDomino.add(0, new FichaDomino(pongo.getInt("numberLeft"), pongo.getInt("numberRight")));
				valido = true;
				// IZQUIERDA-IZQUIERDA (vuelta)
			} else if (pongo.getInt("numberLeft") == juntoA.getInt("numberLeft")) {
				this.tableroDomino.add(0, new FichaDomino(pongo.getInt("numberRight"), pongo.getInt("numberLeft")));
				valido = true;
			}

		} else if (juntoA.getString("ocupadoLeft").equals("true")) {

			// IZQUIERDA-DERECHA
			if (pongo.getInt("numberLeft") == juntoA.getInt("numberRight")) {
				this.tableroDomino.add(new FichaDomino(pongo.getInt("numberLeft"), pongo.getInt("numberRight")));
				valido = true;
			}
			// DERECHA-DERECHA (vuelta)
			else if (pongo.getInt("numberRight") == juntoA.getInt("numberRight")) {
				this.tableroDomino.add(new FichaDomino(pongo.getInt("numberRight"), pongo.getInt("numberLeft")));
				valido = true;
			}
		}

		if (valido) {
			this.actualizarTableros();
		} else {
			throw new Exception("Movimiento no válido");
		}

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

}
