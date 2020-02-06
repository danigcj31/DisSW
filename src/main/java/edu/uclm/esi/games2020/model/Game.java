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
		if (match==null) {
			match = buildMatch();
			this.pendingMatches.add(match);
		} else {
			match.addPlayer(user);
			if (match.getPlayers().size() == this.requiredPlayers) {
				this.pendingMatches.poll();
				this.inPlayMatches.put(match.getId(), match);
				match.start();
			}
		}
		return match;
	}

	protected abstract Match buildMatch();
	
}
