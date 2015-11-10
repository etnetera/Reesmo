package com.etnetera.tremapp.model.form.user;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.etnetera.tremapp.user.UserType;

public class ManualUserCommand extends UserCommand implements ManualUserCommandInterface {

	@NotBlank
	@Email
	private String email;

	@Override
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public UserType getType() {
		return UserType.MANUAL;
	}
	
}
