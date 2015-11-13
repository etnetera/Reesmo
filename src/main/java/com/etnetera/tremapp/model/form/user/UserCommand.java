package com.etnetera.tremapp.model.form.user;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.etnetera.tremapp.model.mongodb.user.ApiUser;
import com.etnetera.tremapp.model.mongodb.user.ManualUser;
import com.etnetera.tremapp.model.mongodb.user.User;
import com.etnetera.tremapp.user.UserType;

public class UserCommand implements UsernameCommand, EmailCommand, PasswordCommand {

	@NotBlank
	@Size(min = 2, max = 255)
	protected String label;

	@Size(min = 2, max = 255)
	protected String username;
	
	private boolean active;

	private boolean superadmin;
	
	@NotNull
	protected String type = UserType.MANUAL.name().toLowerCase();
	
	@Size(min = 4, max = 255)
	protected String password;
	
	@Size(min = 4, max = 255)
	protected String passwordConfirm;
	
	@Email
	protected String email;
	
	protected List<String> allowedIps;
	
	public void updateFromUser(User user) {
		label = user.getLabel();
		username = user.getUsername();
		active = user.isActive();
		superadmin = user.isSuperadmin();
		type = user.getType().name().toLowerCase();
		if (user instanceof ManualUser) {
			fromUser((ManualUser) user);
		} else if (user instanceof ApiUser) {
			fromUser((ApiUser) user);
		}
	}
	
	private void fromUser(ManualUser user) {
		email = user.getEmail();
	}
	
	private void fromUser(ApiUser user) {
		allowedIps = user.getAllowedIps();
	}

	public void propagateToUser(User user, boolean create, boolean sameUserLoggedUser) {
		user.setLabel(label);
		user.setUsername(username);
		if (!sameUserLoggedUser) {
			user.setActive(active);
			user.setSuperadmin(superadmin);
		}
		if (create) {
			BCryptPasswordEncoder passEncoder = new BCryptPasswordEncoder();
			user.setPassword(passEncoder.encode(password));
		}
		
		if (user instanceof ManualUser) {
			toUser((ManualUser) user, create, sameUserLoggedUser);
		} else if (user instanceof ApiUser) {
			toUser((ApiUser) user, create, sameUserLoggedUser);
		}
	}
	
	protected void toUser(ManualUser user, boolean create, boolean sameUserLoggedUser) {
		if (!UserType.MANUAL.is(type)) {
			throw new IllegalArgumentException("Manual user can not be injected from " + type + " type!");
		}
		user.setEmail(email);
	}
	
	protected void toUser(ApiUser user, boolean create, boolean sameUserLoggedUser) {
		if (!UserType.API.is(type)) {
			throw new IllegalArgumentException("Api user can not be injected from " + type + " type!");
		}
		if (!sameUserLoggedUser) {
			user.setAllowedIps(allowedIps);
		}
	}

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getAllowedIps() {
		return allowedIps;
	}

	public void setAllowedIps(List<String> allowedIps) {
		this.allowedIps = allowedIps;
	}

}
