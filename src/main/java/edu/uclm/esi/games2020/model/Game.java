package edu.uclm.esi.games2020.model;

import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class Game {
	protected int requiredPlayers;
	protected Queue<Match> pendingMatches;
	protected ConcurrentHashMap<String, Match> inPlayMatches;
	
	public Game(int requiredPlayers) {
		this.requiredPlayers = requiredPlayers;
		this.pendingMatches = new LinkedBlockingQueue<>();
		this.inPlayMatches = new ConcurrentHashMap<>();
	}

	public abstract String getName();

	public Match startMatch(User user) {
		Match match = this.pendingMatches.peek();
		/*if (match==null) {
			// crear partida
		} else {
			match.addPlayer(user);
			if (isComplete(match)) {
				
			}
		}*/
		return match;
	}
	
}
