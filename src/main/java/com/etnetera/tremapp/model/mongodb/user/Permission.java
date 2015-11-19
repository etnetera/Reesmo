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
	 * Editor plus project management rights
	 */
	ADMIN(30),
	
	/**
	 * Admin plus project owner management and able to delete project
	 */
	OWNER(40);
	
	private int priority;
	
	private Permission(int priority) {
		this.priority = priority;
	}
	
	public boolean isGreaterThan(Permission permission) {
		return permission == null || priority > permission.priority;
	}
	
	public boolean isGreaterThanOrEqual(Permission permission) {
		return permission == null || priority >= permission.priority;
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
