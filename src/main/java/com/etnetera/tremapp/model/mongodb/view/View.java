package com.etnetera.tremapp.model.mongodb.view;

import java.util.List;

import org.joda.time.Interval;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.etnetera.tremapp.model.elasticsearch.result.Result;
import com.etnetera.tremapp.model.mongodb.MongoAuditedModel;
import com.etnetera.tremapp.model.mongodb.user.Permission;
import com.etnetera.tremapp.model.mongodb.user.User;
import com.etnetera.tremapp.user.ForbiddenException;
import com.etnetera.tremapp.utils.list.ListModifier;

/**
 * Describes stored view representing list modifier.
 * 
 * @author zdenek
 * 
 */
@Document
public class View extends MongoAuditedModel {

	@Id
	private String id;

	/**
	 * Unique not required key for API access. Is not ID so it can be changed if
	 * needed.
	 */
	@Indexed(unique = true)
	private String key;

	private String name;

	private String description;

	private ListModifier modifier;

	/**
	 * If true then is available for everyone in list of views.
	 */
	private boolean shared;

	/**
	 * List of users with this view registered
	 */
	@DBRef
	private List<User> users;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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

	public ListModifier getModifier() {
		return modifier;
	}

	public void setModifier(ListModifier modifier) {
		this.modifier = modifier;
	}

	public boolean isShared() {
		return shared;
	}

	public void setShared(boolean shared) {
		this.shared = shared;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<Result> getResults() {
		// TODO apply all modifiers and limit
		return null;
	}

	public List<Result> getChangedResults(Interval interval) {
		// TODO get results but include results with update time in given
		// interval only
		return null;
	}

	public void checkUserPermission(User user, Permission permission) {
		if (!isAllowedForUser(user, permission)) {
			throw new ForbiddenException("User with id " + user == null ? null
					: user.getId() + " has not " + permission + " permission for view with id " + getId() + ".");
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
		switch (permission) {
		case BASIC:
			// view is visible if shared or user is in list of assigned users
			return shared || (users != null && users.stream().anyMatch(u -> u.getId().equals(user.getId())));
		case EDITOR:
			// view is editable only by author
			return user.getId().equals(getCreatedAt());
		default:
			return false;
		}
	}

}
