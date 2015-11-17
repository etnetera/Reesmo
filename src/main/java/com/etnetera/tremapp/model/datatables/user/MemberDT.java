package com.etnetera.tremapp.model.datatables.user;

import java.util.Locale;

import com.etnetera.tremapp.message.Localizer;
import com.etnetera.tremapp.model.mongodb.user.Permission;
import com.etnetera.tremapp.model.mongodb.user.User;

abstract public class MemberDT {

	protected String userId;
	
	protected String userLabel;
	
	protected String userUsername;
	
	protected String userType;
	
	protected String permission;
	
	public MemberDT(User user, Permission permission, Localizer localizer, Locale locale) {
		this.userId = user.getId();
		this.userLabel = user.getLabel();
		this.userUsername = user.getUsername();
		this.userType = localizer.localize(user.getType(), locale);
		this.permission = localizer.localize(permission, locale);
	}

	public String getUserId() {
		return userId;
	}

	public String getUserLabel() {
		return userLabel;
	}

	public String getUserUsername() {
		return userUsername;
	}

	public String getUserType() {
		return userType;
	}

	public String getPermission() {
		return permission;
	}

}
