package com.etnetera.tremapp.model.mongodb.user;

public enum Permission {
	
	/**
	 * Read-only access
	 */
	BASIC(10),
	
	/**
	 * Read/write access
	 */
	EDITOR(20),
	
	/**
	 * Editor plus user management rights
	 */
	ADMIN(30);
	
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
	
	public static Permission fromString(String value) {
		for (Permission p : Permission.values()) {
			if (p.name().equalsIgnoreCase(value)) {
				return p;
			}
		}
		return null;
	}
	
	public boolean is(String value) {
		return name().equalsIgnoreCase(value);
	}
	
}
