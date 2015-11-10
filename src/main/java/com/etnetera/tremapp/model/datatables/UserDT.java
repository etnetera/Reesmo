package com.etnetera.tremapp.model.datatables;

import com.etnetera.tremapp.model.mongodb.user.User;
import com.etnetera.tremapp.user.UserType;

public class UserDT extends AuditedModelDT {

	private String id;
	
	private String label;
	
	private String username;
	
	private UserType type;
	
	private boolean active;
	
	private boolean superadmin;
	
	public UserDT(User user) {
		super(user);
		this.id = user.getId();
		this.label = user.getLabel();
		this.username = user.getUsername();
		this.type = user.getType();
		this.active = user.isActive();
		this.superadmin = user.isSuperadmin();
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

	public UserType getType() {
		return type;
	}

	public boolean isActive() {
		return active;
	}

	public boolean isSuperadmin() {
		return superadmin;
	}
	
}
