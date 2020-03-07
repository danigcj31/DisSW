package edu.uclm.esi.games2020.model;

import edu.uclm.esi.games2020.model.Game;
import edu.uclm.esi.games2020.model.Match;

public class TresEnRayaGame extends Game {

	public TresEnRayaGame() {
		super(2);
	}

	@Override
	public String getName() {
		return "Tres en raya";
	}

	@Override
	protected Match buildMatch() {
		return new TresEnRayaMatch();
	}

}