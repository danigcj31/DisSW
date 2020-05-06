package edu.uclm.esi.games2020.exceptions;

public class NoTienesElTurnoException extends Exception {

	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	public NoTienesElTurnoException() {
		super("No tienes el turno");
	}

}