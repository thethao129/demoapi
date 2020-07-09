package com.example.Ecabinet.dto;

import java.io.Serializable;

public class JwtResponse implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -549044832879618887L;
	private String token;

	public JwtResponse(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public JwtResponse() {
		super();
	}

	
	
	
	
	
}
