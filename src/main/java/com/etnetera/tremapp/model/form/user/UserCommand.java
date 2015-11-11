package com.etnetera.tremapp.model.form.user;

import java.util.List;

import javax.validation.constraints.Size;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.etnetera.tremapp.model.mongodb.user.ApiUser;
import com.etnetera.tremapp.model.mongodb.user.ManualUser;
import com.etnetera.tremapp.model.mongodb.user.User;

public class UserCommand extends UserProfileCommand {

	private boolean active;

	private boolean superadmin;
	
	private List<String> allowedIps;
	
	@Size(min = 4, max = 255)
	private String password;
	
	@Size(min = 4, max = 255)
	private String passwordConfirm;
	
	@Override
	public void updateFromUser(User user) {
		super.updateFromUser(user);
		if (user instanceof ApiUser) {
			fromUser((ApiUser) user);
		}
	}

	@Override
	public void propagateToUser(User user) {
		propagateToUser(user, false);
	}
	
	public void propagateToUser(User user, boolean create) {
		if (user instanceof ManualUser) {
			toUser((ManualUser) user, create);
		} else if (user instanceof ApiUser) {
			toUser((ApiUser) user, create);
		}
	}

	@Override
	protected void fromUser(User user) {
		super.fromUser(user);
		active = user.isActive();
		superadmin = user.isSuperadmin();
	}
	
	protected void fromUser(ApiUser user) {
		fromUser((User) user);
		allowedIps = user.getAllowedIps();
	}
	
	@Override
	protected void toUser(User user) {
		toUser(user, false);
	}
	
	protected void toUser(User user, boolean create) {
		super.toUser(user);
		user.setActive(active);
		user.setSuperadmin(superadmin);
		
		if (create) {
			BCryptPasswordEncoder passEncoder = new BCryptPasswordEncoder();
			user.setPassword(passEncoder.encode(password));
		}
	}
	
	@Override
	protected void toUser(ManualUser user) {
		toUser(user, false);
	}
	
	protected void toUser(ManualUser user, boolean create) {
		toUser((User) user, create);
		super.toUser(user);
	}
	
	protected void toUser(ApiUser user) {
		toUser(user, false);
	}
	
	protected void toUser(ApiUser user, boolean create) {
		toUser((User) user, create);
		user.setAllowedIps(allowedIps);
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
	
	public List<String> getAllowedIps() {
		return allowedIps;
	}

	public void setAllowedIps(List<String> allowedIps) {
		this.allowedIps = allowedIps;
	}

}
