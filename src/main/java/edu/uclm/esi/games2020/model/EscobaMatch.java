package edu.uclm.esi.games2020.model;

public class EscobaMatch extends Match {
	private Deck deck;

	public EscobaMatch() {
		super();
		this.deck = new Deck();
		this.deck.suffle();
	}
}
