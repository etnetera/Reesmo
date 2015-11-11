package com.etnetera.tremapp.model.mongodb.user;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.etnetera.tremapp.user.UserRole;
import com.etnetera.tremapp.user.UserType;

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
		return UserRole.ROLE_APIUSER;
	}

	@Override
	public UserType getType() {
		return UserType.API;
	}
	
}
