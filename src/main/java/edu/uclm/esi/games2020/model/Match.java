package edu.uclm.esi.games2020.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class Match {
	protected List<User> players;
	protected String id;
	
	public Match() {
		this.id = UUID.randomUUID().toString();
		this.players = new ArrayList<>();
	}

	public void addPlayer(User user) {
		this.players.add(user);
	}

	public List<User> getPlayers() {
		return players;
	}
	
	public String getId() {
		return id;
	}

	public abstract void start();
}
