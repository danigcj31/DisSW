package edu.uclm.esi.games2020.model;

public class TerState implements IState {

	private User user;
	boolean estado = false;
	
	@Override
	public void setUser(User user) {
		this.user = user;
	}
	
}
