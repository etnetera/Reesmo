package com.etnetera.tremapp.web.model.mongodb.user;

public enum Permission {

	/**
	 * Read-only access
	 */
	BASIC(0),
	
	/**
	 * Read/write access
	 */
	EDITOR(10),
	
	/**
	 * Editor plus user management rights
	 */
	ADMIN(20);
	
	private int priority;
	
	private Permission(int priority) {
		this.priority = priority;
	}
	
	public boolean isGreaterThan(Permission permission) {
		return priority > permission.priority;
	}
	
	public boolean isGreaterThanOrEqual(Permission permission) {
		return priority >= permission.priority;
	}
	
}
