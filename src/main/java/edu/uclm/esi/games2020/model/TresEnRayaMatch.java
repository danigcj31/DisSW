package edu.uclm.esi.games2020.model;

import java.util.Random;
import org.json.JSONObject;
import edu.uclm.esi.games2020.model.Game;
import edu.uclm.esi.games2020.model.Match;
import edu.uclm.esi.games2020.model.TresEnRayaBoard;

public class TresEnRayaMatch extends Match {
	
	private TresEnRayaBoard board;
	private User user;

	public TresEnRayaMatch(Game game) {
		super();
		this.board = new TresEnRayaBoard(this);
		calculateFirstPlayer();
	}

	public void calculateFirstPlayer() {
		boolean dado=new Random().nextBoolean();
		this.currentPlayer = dado ? 0 : 1;
		this.currentPlayer = 0;
	}

	protected boolean tieneElTurno(User user) {
		return (this.getCurrentPlayer()==0 && user==this.playerA) || (this.getCurrentPlayer()==1 && user==playerB);
	}

	public int getCurrentPlayer() {
		return 0;
	}

	@Override
	protected void setState(User user) {
		// TODO Auto-generated method stub
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
	}

	@Override
	protected JSONObject startData(User player) {
		// TODO Auto-generated method stub
		return null;
	}
}

