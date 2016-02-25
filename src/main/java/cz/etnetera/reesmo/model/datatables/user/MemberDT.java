package cz.etnetera.reesmo.model.datatables.user;

import java.util.Locale;

import cz.etnetera.reesmo.message.Localizer;
import cz.etnetera.reesmo.model.mongodb.user.Permission;
import cz.etnetera.reesmo.model.mongodb.user.User;

abstract public class MemberDT {

	protected String id;
	
	protected String label;
	
	protected String username;
	
	protected String type;
	
	protected String permission;
	
	public MemberDT(User user, Permission permission, Localizer localizer, Locale locale) {
		this.id = user.getId();
		this.label = user.getLabel();
		this.username = user.getUsername();
		this.type = localizer.localize(user.getType(), locale);
		this.permission = localizer.localize(permission, locale);
	}

	public String getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public String getUsername() {
		return username;
	}

	public String getType() {
		return type;
	}

	public String getPermission() {
		return permission;
	}

}
