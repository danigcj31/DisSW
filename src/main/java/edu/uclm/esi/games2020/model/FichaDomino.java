package edu.uclm.esi.games2020.model;

import org.json.JSONObject;

public class FichaDomino {
	private int numberL;
	private int numberR;
	private IState state;
	
	public FichaDomino(int numberL,int numberR) {
		super();
		this.numberL = numberL;
		this.numberR = numberR;
	}

	public JSONObject toJSON() {
		JSONObject jso = new JSONObject();
		jso.put("numberLeft", this.numberL);
		jso.put("numberRight", this.numberR);
		return jso;
	}

	public void setState(IState state) {
		this.state = state;
	}

	public int getNumberL() {
		return numberL;
	}

	public void setNumberL(int numberL) {
		this.numberL = numberL;
	}

	public int getNumberR() {
		return numberR;
	}

	public void setNumberR(int numberR) {
		this.numberR = numberR;
	}

}
