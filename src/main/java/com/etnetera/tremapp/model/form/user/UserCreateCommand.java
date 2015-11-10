package com.etnetera.tremapp.model.form.user;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

abstract public class UserCreateCommand extends UserCommand {
	
	@NotBlank
	@Size(min = 4, max = 255)
	private String password;
	
	@NotBlank
	@Size(min = 4, max = 255)
	private String passwordConfirm;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}
		
}
