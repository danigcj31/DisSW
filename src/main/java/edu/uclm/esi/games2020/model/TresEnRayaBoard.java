package edu.uclm.esi.games2020.model;

import org.json.JSONObject;

public class TresEnRayaBoard {
	private Character[][] squares;
	private Match match;
	
	public TresEnRayaBoard(TresEnRayaMatch match) {
		this.squares=new Character[3][3];
	}
	
	public JSONObject toJSON() throws Exception {
		JSONObject jso=new JSONObject();
		jso.put("idBoard", this.match.getId()).put("type", this.getClass().getSimpleName()).put("squares", this.getContent());
		return jso;
	}
	
	public Character[][] getSquares() {
		return squares;
	}

	public void move(User user, Integer[] coordinates) throws Exception {
		char symbol = (this.match.getCurrentPlayer()==0 ? 'X' : 'O');
		int row=coordinates[0];
		int col=coordinates[1];
		if (squares[row][col]!=null)
			throw new Exception("Square occupied");
		squares[row][col]=symbol;
	}

	public User getWinner() {
		User player=this.match.getCurrentPlayer()==0 ? this.match.getPlayerA() : this.match.getPlayerB();
		char symbol=this.match.getCurrentPlayer()==0 ? 'X' : 'O';
		
		int i=0;
		boolean ristra1=squares[i][0]!=null && squares[i][0]==symbol &&  
				squares[i][1]!=null && squares[i][1]==symbol && 
				squares[i][2]!=null && squares[i][2]==symbol;
		i=1;
		boolean ristra2=squares[i][0]!=null && squares[i][0]==symbol &&  
				squares[i][1]!=null && squares[i][1]==symbol && 
				squares[i][2]!=null && squares[i][2]==symbol;
		i=2;
		boolean ristra3=squares[i][0]!=null && squares[i][0]==symbol &&  
				squares[i][1]!=null && squares[i][1]==symbol && 
				squares[i][2]!=null && squares[i][2]==symbol;
		if (ristra1 || ristra2 || ristra3)
			return player;
		i=0;
		ristra1=squares[0][i]!=null && squares[0][i]==symbol &&  
				squares[1][i]!=null && squares[1][i]==symbol && 
				squares[2][i]!=null && squares[2][i]==symbol;
		i=1;
		ristra2=squares[0][i]!=null && squares[0][i]==symbol &&  
				squares[1][i]!=null && squares[1][i]==symbol && 
				squares[2][i]!=null && squares[2][i]==symbol;
		i=2;
		ristra3=squares[0][i]!=null && squares[0][i]==symbol &&  
				squares[1][i]!=null && squares[1][i]==symbol && 
				squares[2][i]!=null && squares[2][i]==symbol;
		if (ristra1 || ristra2 || ristra3)
			return player;
		ristra1=squares[0][0]!=null && squares[0][0]==symbol &&  
				squares[1][1]!=null && squares[1][1]==symbol && 
				squares[2][2]!=null && squares[2][2]==symbol;
		ristra2=squares[2][0]!=null && squares[2][0]==symbol &&  
				squares[1][1]!=null && squares[1][1]==symbol && 
				squares[0][2]!=null && squares[0][2]==symbol;
		if (ristra1 || ristra2)
			return player;
		return null;
	}

	public boolean end() {
		return false;
	}

	public String getContent() {
		String r="";
		for (int i=0; i<this.squares.length; i++) {
			for (int j=0; j<this.squares[i].length; j++) 
				r+=this.squares[i][j]==null ? ' ' : this.squares[i][j];
		}
		return r;
	}

	public boolean draw() {
		for (int i=0; i<this.squares.length; i++)
			for (int j=0; j<this.squares.length; j++)
				if (this.squares[i][j]==null)
					return false;
		return true;
	}
}
