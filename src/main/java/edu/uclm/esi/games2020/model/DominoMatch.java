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

	public DominoMatch() {
		super();
		this.tacoFichas = new TacoFichas();
		this.tacoFichas.suffle();
		this.fichasMesa = new ArrayList<>();
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
	protected void mover(User user, String movimiento) throws Exception {
	if (user != this.jugadorConElTurno)
			throw new Exception("No tienes el turno");
		int posicion = Integer.parseInt(movimiento);
		if (!this.tablero.get(posicion).equals("-"))
			throw new Exception("Casilla ocupada");
		this.tablero.set(posicion, getFicha());
		// actualizar los tableros a los clientes
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

}
