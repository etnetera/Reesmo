package com.etnetera.tremapp.model.form.user;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.etnetera.tremapp.model.mongodb.user.ApiUser;
import com.etnetera.tremapp.model.mongodb.user.ManualUser;
import com.etnetera.tremapp.model.mongodb.user.User;
import com.etnetera.tremapp.user.UserType;

public class UserProfileCommand implements UsernameCommand, EmailCommand {

	@NotBlank
	@Size(min = 2, max = 255)
	protected String label;

	@NotBlank
	@Size(min = 2, max = 255)
	protected String username;
	
	@NotNull
	protected UserType type;
	
	@Email
	protected String email;
	
	public void updateFromUser(User user) {
		if (user instanceof ManualUser) {
			fromUser((ManualUser) user);
		}
	}
	
	public void propagateToUser(User user) {
		if (user instanceof ManualUser) {
			toUser((ManualUser) user);
		} else if (user instanceof ApiUser) {
			toUser((ApiUser) user);
		}
	}
	
	protected void fromUser(User user) {
		label = user.getLabel();
		username = user.getUsername();
		type = user.getType();
	}
	
	protected void fromUser(ManualUser user) {
		fromUser((User) user);
		email = user.getEmail();
	}
	
	protected void toUser(User user) {
		user.setLabel(label);
		user.setUsername(username);
	}
	
	protected void toUser(ManualUser user) {
		if (!UserType.MANUAL.equals(type)) {
			throw new IllegalArgumentException("Manual user can not injected from " + type + " type!");
		}
		toUser((User) user);
		user.setEmail(email);
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

	public UserType getType() {
		return type;
	}

	public void setType(UserType type) {
		this.type = type;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
