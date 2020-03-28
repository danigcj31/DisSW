package edu.uclm.esi.games2020.model;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class TacoFichas {
	private List<FichaDomino> fichasDomino;

	public TacoFichas() {
		this.fichasDomino = new ArrayList<>();
		
		this.fichasDomino.add(new FichaDomino(0,0));
		this.fichasDomino.add(new FichaDomino(0,1));
		this.fichasDomino.add(new FichaDomino(0,2));
		this.fichasDomino.add(new FichaDomino(0,3));
		this.fichasDomino.add(new FichaDomino(0,4));
		this.fichasDomino.add(new FichaDomino(0,5));
		this.fichasDomino.add(new FichaDomino(0,6));
		this.fichasDomino.add(new FichaDomino(1,1));
		this.fichasDomino.add(new FichaDomino(1,2));
		this.fichasDomino.add(new FichaDomino(1,3));
		this.fichasDomino.add(new FichaDomino(1,4));
		this.fichasDomino.add(new FichaDomino(1,5));
		this.fichasDomino.add(new FichaDomino(1,6));
		this.fichasDomino.add(new FichaDomino(2,2));
		this.fichasDomino.add(new FichaDomino(2,3));
		this.fichasDomino.add(new FichaDomino(2,4));
		this.fichasDomino.add(new FichaDomino(2,5));
		this.fichasDomino.add(new FichaDomino(2,6));
		this.fichasDomino.add(new FichaDomino(3,3));
		this.fichasDomino.add(new FichaDomino(3,4));
		this.fichasDomino.add(new FichaDomino(3,5));
		this.fichasDomino.add(new FichaDomino(3,6));
		this.fichasDomino.add(new FichaDomino(4,4));
		this.fichasDomino.add(new FichaDomino(4,5));
		this.fichasDomino.add(new FichaDomino(4,6));
		this.fichasDomino.add(new FichaDomino(5,5));
		this.fichasDomino.add(new FichaDomino(5,6));
		this.fichasDomino.add(new FichaDomino(6,6));
	}

	

	public void suffle() {
		SecureRandom dado = new SecureRandom();
		for (int i = 0; i < 200; i++) {
			int a = dado.nextInt(28);
			int b = dado.nextInt(28);
			FichaDomino auxiliar = this.fichasDomino.get(a);
			this.fichasDomino.set(a, this.fichasDomino.get(b));
			this.fichasDomino.set(b, auxiliar);
		}
	}

	public FichaDomino getFichaDomino() {
		return this.fichasDomino.remove(0);
	}
}
