package edu.uclm.esi.games2020.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class EscobaMatch extends Match {
	private Deck deck;
	private List<Card> cardsOnTable;

	public EscobaMatch() {
		super();
		this.deck = new Deck();
		this.deck.suffle();
		this.cardsOnTable = new ArrayList<>();
		this.cardsOnTable.add(this.deck.getCard());
		this.cardsOnTable.add(this.deck.getCard());
		this.cardsOnTable.add(this.deck.getCard());
		this.cardsOnTable.add(this.deck.getCard());
	}

	@Override
	public void start() {
		this.started = true;
		super.notifyStart();
	}

	@Override
	protected JSONObject startData(User player) {
		Card card1 = this.deck.getCard();
		Card card2 = this.deck.getCard();
		Card card3 = this.deck.getCard();
		card1.setState(player.getState());
		card2.setState(player.getState());
		card3.setState(player.getState());
		JSONArray jsaCartasDelJugador = new JSONArray();
		jsaCartasDelJugador.put(card1.toJSON());
		jsaCartasDelJugador.put(card2.toJSON());
		jsaCartasDelJugador.put(card3.toJSON());
		
		JSONObject jso = new JSONObject();
		JSONArray jsaCartasMesa = new JSONArray();
		for (Card card : this.cardsOnTable)
			jsaCartasMesa.put(card.toJSON());
		
		jso.put("table", jsaCartasMesa);
		jso.put("data", jsaCartasDelJugador);
		return jso;
	}

	@Override
	protected void setState(User user) {
		IState state = new EscobaState();
		user.setState(state);
		state.setUser(user);
	}
}
