package edu.uclm.esi.games2020.model;

import java.io.IOException;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;


@Entity
public class User {
	@Id
	@Column(name="nombre_usuario")
	private String userName;
	private String email;
	private String pwd;
	@OneToMany
	private List<Match> matches;
	@Transient
	private WebSocketSession session;
	@Transient
	private IState state;
	@Transient
	private HttpSession httpSession;
	
	public void setState(IState state) {
		this.state = state;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public JSONObject toJSON() {
		return new JSONObject().put("userName", this.userName);
	}

	public void setSession(WebSocketSession session) {
		this.session = session;
	}

	public void send(JSONObject json) throws IOException {
		this.session.sendMessage(new TextMessage(json.toString()));		
	}

	public IState getState() {
		return this.state;
	}

	public void setHttpSession(HttpSession httpSession) {
		this.httpSession = httpSession;
	}
	
	public HttpSession getHttpSession() {
		return httpSession;
	}

	public String getPwd() {
		return this.pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
}
