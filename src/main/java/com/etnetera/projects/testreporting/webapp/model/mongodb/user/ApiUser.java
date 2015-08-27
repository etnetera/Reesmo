package com.etnetera.projects.testreporting.webapp.model.mongodb.user;

import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ApiUser extends User {

	@Indexed(unique = true)
	private String authToken;
	
	private List<String> allowedIps;

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public List<String> getAllowedIps() {
		return allowedIps;
	}

	public void setAllowedIps(List<String> allowedIps) {
		this.allowedIps = allowedIps;
	}
	
}
