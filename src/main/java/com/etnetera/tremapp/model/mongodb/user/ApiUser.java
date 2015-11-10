package com.etnetera.tremapp.model.mongodb.user;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.etnetera.tremapp.user.UserRole;
import com.etnetera.tremapp.user.UserType;

@Document(collection = "user")
public class ApiUser extends User {
	
	public static final String TYPE = "api";
	
	private List<String> allowedIps;

	public List<String> getAllowedIps() {
		return allowedIps;
	}

	public void setAllowedIps(List<String> allowedIps) {
		this.allowedIps = allowedIps;
	}

	@Override
	public UserRole getRole() {
		return UserRole.APIUSER;
	}

	@Override
	public UserType getType() {
		return UserType.API;
	}
	
}
