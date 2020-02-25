package edu.uclm.esi.games2020.model;

public class DominoGame extends Game {

	public DominoGame() {
		super(2);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Domino";
	}

	@Override
	protected Match buildMatch() {
		return new DominoMatch();
	}

}
