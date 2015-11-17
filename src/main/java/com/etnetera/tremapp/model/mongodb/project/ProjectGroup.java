package com.etnetera.tremapp.model.mongodb.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.etnetera.tremapp.http.exception.ForbiddenException;
import com.etnetera.tremapp.model.mongodb.MongoAuditedModel;
import com.etnetera.tremapp.model.mongodb.user.Permission;
import com.etnetera.tremapp.model.mongodb.user.User;

@Document
public class ProjectGroup extends MongoAuditedModel {

	@Id
	private String id;
	
	private String name;
	
	private String description;
	
	private List<String> projects = new ArrayList<>();
	
	private Map<String, Permission> members = new HashMap<>();

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

	public Map<String, Permission> getMembers() {
		return members;
	}

	public void setMembers(Map<String, Permission> members) {
		this.members = members;
	}
	
	public void checkUserPermission(User user, Permission permission) {
		if (!isAllowedForUser(user, permission)) {
			throw new ForbiddenException("User with id " + user == null ? null
					: user.getId() + " has not " + permission + " permission for project group with id " + getId() + ".");
		}
	}

	public boolean isAllowedForUser(User user, Permission permission) {
		if (user == null) {
			return false;
		}
		if (user.isSuperadmin()) {
			return true;
		}
		if (permission == null) {
			return false;
		}
		Permission userPermission = members.get(user.getId());
		if (userPermission != null && userPermission.isGreaterThanOrEqual(permission)) {
			return true;
		}
		return false;
	}
	
}
