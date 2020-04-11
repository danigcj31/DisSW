package edu.uclm.esi.games2020.model;

import java.util.ArrayList;
import java.util.List;

public class DominoState implements IState {

	private User user;
	private List<FichaDomino> fichas;
	private boolean pasarTurnoEmpate;

	
	public void addFichas(FichaDomino... fichas) {
		for (int i=0; i<fichas.length; i++)
			this.fichas.add(fichas[i]);
	}

	public int getNumFichas() {
		return this.fichas.size();
	}
	
	public boolean getPasarTurnoEmpate() {
		return pasarTurnoEmpate;
	}
	
	public FichaDomino getUltFicha() {
		return this.fichas.get(this.fichas.size()-1);
	}
	
	public void removeFichas(FichaDomino ficha) {
		this.fichas.remove(ficha);
	}
	
	public void setPasarTurnoEmpate(boolean pasarTurnoEmpate) {
		this.pasarTurnoEmpate = pasarTurnoEmpate;
	}	
	
	@Override
	public void setUser(User user) {
		this.fichas = new ArrayList<>();
		this.user = user;
	}
}
