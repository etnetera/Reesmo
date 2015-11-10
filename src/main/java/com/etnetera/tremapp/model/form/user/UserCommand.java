package com.etnetera.tremapp.model.form.user;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.etnetera.tremapp.user.UserType;

abstract public class UserCommand {
	
	@NotBlank
	@Size(min = 2, max = 255)
	private String label;
	
	@NotBlank
	@Size(min = 2, max = 255)
	private String username;
	
	private boolean active;
	
	private boolean superadmin;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isSuperadmin() {
		return superadmin;
	}

	public void setSuperadmin(boolean superadmin) {
		this.superadmin = superadmin;
	}
	
	abstract public UserType getType();
	
}
