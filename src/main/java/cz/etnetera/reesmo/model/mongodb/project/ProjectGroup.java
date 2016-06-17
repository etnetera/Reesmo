package cz.etnetera.reesmo.model.mongodb.project;

import cz.etnetera.reesmo.http.exception.ForbiddenException;
import cz.etnetera.reesmo.model.mongodb.MongoAuditedModel;
import cz.etnetera.reesmo.model.mongodb.user.Permission;
import cz.etnetera.reesmo.user.IdentifiedUser;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document
public class ProjectGroup extends MongoAuditedModel {

	@Id
	private String id;
	
	private String name;
	
	private String description;
	
	private List<String> projects = new ArrayList<>();
	
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getProjects() {
		return projects;
	}

	public void setProjects(List<String> projects) {
		this.projects = projects;
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
					: user.getId() + " has not " + permission + " permission for project group with id " + getId() + ".");
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
		Permission userPermission = users.get(user.getId());
		if (userPermission != null && userPermission.isGreaterThanOrEqual(permission)) {
			return true;
		}
		return false;
	}
	
	public void checkUserPermission(IdentifiedUser user, String permission) {
		checkUserPermission(user, Permission.fromString(permission));
	}
	
	public boolean isAllowedForUser(IdentifiedUser user, String permission) {
		return isAllowedForUser(user, Permission.fromString(permission));
	}
	
}
