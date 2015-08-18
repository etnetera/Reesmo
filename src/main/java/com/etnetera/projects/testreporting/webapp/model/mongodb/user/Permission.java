package com.etnetera.projects.testreporting.webapp.model.mongodb.user;

public enum Permission {

	/**
	 * Read-only access
	 */
	BASIC,
	
	/**
	 * Read/write access
	 */
	EDITOR,
	
	/**
	 * Editor plus user management rights
	 */
	ADMIN
	
}
