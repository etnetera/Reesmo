package com.etnetera.tremapp.user;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.etnetera.tremapp.http.exception.UnauthorizedException;
import com.etnetera.tremapp.model.mongodb.user.Permission;
import com.etnetera.tremapp.model.mongodb.user.User;
import com.etnetera.tremapp.model.mongodb.view.View;

public class UserHelper {

	public static List<String> getAllowedProjectIds(Permission permission) {
		return requireAppUser().getUser().getAllowedProjectsIds(permission);
	}
	
	public static void checkProjectPermission(String projectId, Permission permission) {
		requireAppUser().checkProjectPermission(projectId, permission);
	}
	
	public static boolean isAllowedForProject(String projectId, Permission permission) {
		return requireAppUser().isAllowedForProject(projectId, permission);
	}
	
	public static List<String> getAllowedProjectGroupIds(Permission permission) {
		return requireAppUser().getUser().getAllowedProjectGroupsIds(permission);
	}
	
	public static void checkProjectGroupPermission(String projectGroupId, Permission permission) {
		requireAppUser().checkProjectGroupPermission(projectGroupId, permission);
	}
	
	public static boolean isAllowedForProjectGroup(String projectGroupId, Permission permission) {
		return requireAppUser().isAllowedForProjectGroup(projectGroupId, permission);
	}
	
	public static void checkViewPermission(View view, Permission permission) {
		view.checkUserPermission(requireAppUser().getUser(), permission);
	}
	
	public static String getUserId() {
		AppUser appUser = getAppUser();
		if (appUser != null) {
			return appUser.getId();
		}
		return null;
	}
	
	public static boolean isSameAsLogged(User user) {
		if (user == null) {
			return false;
		}
		return user.getId().equals(getUserId());
	}
	
	public static User requireUser() {
		return requireAppUser().getUser();
	}
	
	public static User getUser() {
		AppUser appUser = getAppUser();
		if (appUser != null) {
			return appUser.getUser();
		}
		return null;
	}
	
	public static void updateUser(User user) {
		requireAppUser().setUser(user);
	}
	
	public static AppUser requireAppUser() {
		AppUser appUser = getAppUser();
		if (appUser == null) {
			throw new UnauthorizedException("App user is empty");
		}
		return appUser;
	}
	
	public static AppUser getAppUser() {
		Authentication auth = getAuthentication();
		if (auth != null) {
			return (AppUser) auth.getPrincipal();
		}
		return null;
	}
	
	public static Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
}
