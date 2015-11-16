package com.etnetera.tremapp.model.form.user;

import javax.validation.constraints.Size;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.etnetera.tremapp.model.mongodb.user.User;

public class UserChangePasswordCommand implements PasswordCommand {
	
	@Size(min = 4, max = 255)
	protected String originalPassword;
	
	@Size(min = 4, max = 255)
	protected String password;
	
	@Size(min = 4, max = 255)
	protected String passwordConfirm;

	public void propagateToUser(User user) {
		BCryptPasswordEncoder passEncoder = new BCryptPasswordEncoder();
		user.setPassword(passEncoder.encode(password));
	}

	public String getOriginalPassword() {
		return originalPassword;
	}

	public void setOriginalPassword(String originalPassword) {
		this.originalPassword = originalPassword;
	}

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
