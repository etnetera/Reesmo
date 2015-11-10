package com.etnetera.tremapp.model.form.user;

import java.util.List;

import com.etnetera.tremapp.user.UserType;

public class ApiUserCreateCommand extends UserCreateCommand implements ApiUserCommandInterface {

	private List<String> allowedIps;

	@Override
	public List<String> getAllowedIps() {
		return allowedIps;
	}

	public void setAllowedIps(List<String> allowedIps) {
		this.allowedIps = allowedIps;
	}
	
	@Override
	public UserType getType() {
		return UserType.API;
	}
	
}
