package com.etnetera.tremapp.user;

public enum UserRole {

	MANUALUSER, APIUSER;
	
	public String getAuthority() {
		return "ROLE_" + name();
	}
	
	public String getRole() {
		return name();
	}
	
}
