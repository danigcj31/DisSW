package edu.uclm.esi.games2020.model;

import java.util.ArrayList;
import java.util.List;

public class DominoState implements IState {

	private User user;
	private List<FichaDomino> fichas;

	@Override
	public void setUser(User user) {
		this.fichas = new ArrayList<>();
		this.user = user;
	}
	
	public void addFichas(FichaDomino... fichas) {
		for (int i=0; i<fichas.length; i++)
			this.fichas.add(fichas[i]);
	}

}
