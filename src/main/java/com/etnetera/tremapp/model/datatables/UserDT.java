package com.etnetera.tremapp.model.datatables;

import java.util.Locale;

import com.etnetera.tremapp.message.Localizer;
import com.etnetera.tremapp.model.mongodb.user.User;

public class UserDT extends AuditedModelDT {

	private String id;
	
	private String label;
	
	private String username;
	
	private String type;
	
	private String active;
	
	private String superadmin;
	
	public UserDT(User user, Localizer localizer, Locale locale) {
		super(user);
		this.id = user.getId();
		this.label = user.getLabel();
		this.username = user.getUsername();
		this.type = localizer.localize(user.getType(), locale);
		this.active = localizer.localize(user.isActive(), locale);
		this.superadmin = localizer.localize(user.isSuperadmin(), locale);
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

	public String getActive() {
		return active;
	}

	public String getSuperadmin() {
		return superadmin;
	}
	
}
