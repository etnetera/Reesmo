package com.etnetera.tremapp.model.mongodb.user;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.etnetera.tremapp.user.UserRole;
import com.etnetera.tremapp.user.UserType;

/**
 * Classic user registered over UI.
 */
@Document(collection = "user")
public class ManualUser extends User {

	@Indexed(unique = true)
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public String getRole() {
		return UserRole.ROLE_MANUALUSER;
	}
	
	@Override
	public UserType getType() {
		return UserType.MANUAL;
	}
	
}
