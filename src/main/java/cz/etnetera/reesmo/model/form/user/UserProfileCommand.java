package cz.etnetera.reesmo.model.form.user;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import cz.etnetera.reesmo.model.mongodb.user.ManualUser;

public class UserProfileCommand implements UsernameCommand, EmailCommand {

	@NotBlank
	@Size(min = 2, max = 255)
	private String label;

	@Pattern(regexp = "^[A-Za-z0-9_\\-\\.]*$", message = "{validator.Pattern.alphaNumericUnderscoreDashDot.message}")
	@Size(min = 2, max = 255)
	private String username;
	
	@Email
	private String email;
	
	public void fromUser(ManualUser user) {
		label = user.getLabel();
		username = user.getUsername();
		email = user.getEmail();
	}
	
	public void toUser(ManualUser user) {
		user.setLabel(label);
		user.setUsername(username);
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
