package edu.uclm.esi.games2020.model;

import java.io.IOException;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Entity
public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int creditCard;
	

	

	@Id
	private String userName;
	private String email;
	private String pwd;
	@Transient
	private WebSocketSession session;
	@Transient
	private boolean turno;
	@Transient
	private IState state;
	@Transient
	private HttpSession httpSession;

	public void send(JSONObject json) throws IOException {
		this.session.sendMessage(new TextMessage(json.toString()));		
	}

	public JSONObject toJSON() {
		return new JSONObject().put("userName", this.userName);
	}
	
	//GETTERS
	public int getCreditCard() {
		return creditCard;
	}
	public String getEmail() {
		return email;
	}
	
	public HttpSession getHttpSession() {
		return httpSession;
	}

	public String getPwd() {
		return this.pwd;
	}

	public IState getState() {
		return this.state;
	}
	
	public boolean getTurno() {
		return turno;
	}
	
	public String getUserName() {
		return userName;
	}
	//SETTERS
	public void setCreditCard(int creditCard) {
		this.creditCard = creditCard;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public void setHttpSession(HttpSession httpSession) {
		this.httpSession = httpSession;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	public void setSession(WebSocketSession session) {
		this.session = session;
	}
	
	public void setState(IState state) {
		this.state = state;
	}

	public void setTurno(boolean turno) {
		this.turno = turno;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}