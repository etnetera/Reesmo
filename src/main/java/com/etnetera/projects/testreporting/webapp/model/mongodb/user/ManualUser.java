package com.etnetera.projects.testreporting.webapp.model.mongodb.user;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

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
	
}
