package cz.etnetera.reesmo.model.form.user;

import java.util.List;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import cz.etnetera.reesmo.model.mongodb.user.ApiUser;
import cz.etnetera.reesmo.model.mongodb.user.ManualUser;
import cz.etnetera.reesmo.model.mongodb.user.User;
import cz.etnetera.reesmo.user.UserType;

public class UserCommand implements UsernameCommand, EmailCommand, PasswordCommand {

	@NotBlank
	@Size(min = 2, max = 255)
	private String label;

	@Pattern(regexp = "^[A-Za-z0-9_\\-\\.]*$", message = "{validator.Pattern.alphaNumericUnderscoreDashDot.message}")
	@Size(min = 2, max = 255)
	private String username;
	
	private boolean enabled;

	private boolean superadmin;
	
	@NotEmpty
	private String type = UserType.MANUAL.name().toLowerCase();
	
	@Size(min = 4, max = 255)
	private String password;
	
	@Size(min = 4, max = 255)
	private String passwordConfirm;
	
	@Email
	private String email;
	
	private List<String> allowedIps;
	
	public void fromUser(User user) {
		label = user.getLabel();
		username = user.getUsername();
		enabled = user.isEnabled();
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

	public void toUser(User user, boolean create, boolean sameUserLoggedUser) {
		user.setLabel(label);
		user.setUsername(username);
		if (!sameUserLoggedUser) {
			user.setEnabled(enabled);
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
	
	private void toUser(ManualUser user, boolean create, boolean sameUserLoggedUser) {
		if (!UserType.MANUAL.is(type)) {
			throw new IllegalArgumentException("Manual user can not be injected from " + type + " type!");
		}
		user.setEmail(email);
	}
	
	private void toUser(ApiUser user, boolean create, boolean sameUserLoggedUser) {
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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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
