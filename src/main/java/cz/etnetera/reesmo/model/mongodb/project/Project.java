package cz.etnetera.reesmo.model.mongodb.project;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import cz.etnetera.reesmo.http.exception.ForbiddenException;
import cz.etnetera.reesmo.model.mongodb.MongoAuditedModel;
import cz.etnetera.reesmo.model.mongodb.user.Permission;
import cz.etnetera.reesmo.user.IdentifiedUser;

@Document
public class Project extends MongoAuditedModel {

	@Id
	private String id;
	
	/**
	 * Is used for filtering mainly.
	 * No need to be unique.
	 */
	private String name;
	
	/**
	 * Is required. It is used in API access or in views.
	 */
	@Indexed(unique = true)
	private String key;
	
	private String description;
	
	private Map<String, Permission> users = new HashMap<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map<String, Permission> getUsers() {
		return users;
	}

	public void setUsers(Map<String, Permission> users) {
		this.users = users;
	}
	
	public void checkUserPermission(IdentifiedUser user, Permission permission) {
		if (!isAllowedForUser(user, permission)) {
			throw new ForbiddenException("User with id " + user == null ? null
					: user.getId() + " has not " + permission + " permission for project with id " + getId() + ".");
		}
	}

	public boolean isAllowedForUser(IdentifiedUser user, Permission permission) {
		if (user == null) {
			return false;
		}
		if (user.isSuperadmin()) {
			return true;
		}
		if (permission == null) {
			return false;
		}
		return user.getUser().isAllowedForProject(id, permission);
	}
	
	public void checkUserPermission(IdentifiedUser user, String permission) {
		checkUserPermission(user, Permission.fromString(permission));
	}
	
	public boolean isAllowedForUser(IdentifiedUser user, String permission) {
		return isAllowedForUser(user, Permission.fromString(permission));
	}
	
}
