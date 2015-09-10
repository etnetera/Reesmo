package com.etnetera.projects.testreporting.webapp.model.mongodb.user;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
public class ApiUser extends User {
	
	private List<String> allowedIps;

	public List<String> getAllowedIps() {
		return allowedIps;
	}

	public void setAllowedIps(List<String> allowedIps) {
		this.allowedIps = allowedIps;
	}

	@Override
	public String getRole() {
		return "ROLE_APIUSER";
	}
	
}
