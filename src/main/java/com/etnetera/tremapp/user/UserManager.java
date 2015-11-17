package com.etnetera.tremapp.user;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.etnetera.tremapp.http.exception.UnauthorizedException;
import com.etnetera.tremapp.model.mongodb.user.Permission;
import com.etnetera.tremapp.model.mongodb.user.User;

@Component
public class UserManager {

	private static UserManager instance;
	
	private UserManager() {
		instance = this;
	}
	
	public static UserManager getInstance() {
		return instance;
	}

	public List<String> getAllowedProjectIds(Permission permission) {
		return requireAppUser().getAllowedProjectsIds(permission);
	}
	
	public void checkProjectPermission(String projectId, Permission permission) {
		requireAppUser().checkProjectPermission(projectId, permission);
	}
	
	public boolean isAllowedForProject(String projectId, Permission permission) {
		return requireAppUser().isAllowedForProject(projectId, permission);
	}
	
	public String requireUserId() {
		return requireAppUser().getId();
	}
	
	public String getUserId() {
		AppUser appUser = getAppUser();
		if (appUser != null) {
			return appUser.getId();
		}
		return null;
	}
	
	public boolean isSameAsLogged(User user) {
		if (user == null) {
			return false;
		}
		return user.getId().equals(getUserId());
	}
	
	public boolean isSuperadmin() {
		return requireUser().isSuperadmin();
	}
	
	public User requireUser() {
		return requireAppUser().getUser();
	}
	
	public User getUser() {
		AppUser appUser = getAppUser();
		if (appUser != null) {
			return appUser.getUser();
		}
		return null;
	}
	
	public void updateUser(User user) {
		requireAppUser().setUser(user);
	}
	
	public AppUser requireAppUser() {
		AppUser appUser = getAppUser();
		if (appUser == null) {
			throw new UnauthorizedException("App user is empty");
		}
		return appUser;
	}
	
	public AppUser getAppUser() {
		Authentication auth = getAuthentication();
		if (auth != null) {
			return (AppUser) auth.getPrincipal();
		}
		return null;
	}
	
	public Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
}
