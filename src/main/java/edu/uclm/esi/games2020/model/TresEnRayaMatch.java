package edu.uclm.esi.games2020.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import edu.uclm.esi.games2020.model.Match;

public class TresEnRayaMatch extends Match {

	private List<Ficha> tablero;
	private User user;

	public TresEnRayaMatch() {
		super();

		this.tablero = new ArrayList<>();
		//for (int i = 0; i < tablero.size(); i++)
		this.tablero.add(new Ficha("-"));
		this.tablero.add(new Ficha("-"));
		this.tablero.add(new Ficha("-"));
		this.tablero.add(new Ficha("-"));
		this.tablero.add(new Ficha("-"));
		this.tablero.add(new Ficha("-"));
		this.tablero.add(new Ficha("-"));
		this.tablero.add(new Ficha("-"));
		this.tablero.add(new Ficha("-"));

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
		
		for (Ficha ficha : this.tablero)
			jsaTablero.put(ficha.toJSON());
		
		jso.put("table", jsaTablero);

		return jso;
	}
}
