package com.etnetera.projects.testreporting.webapp.model.mongodb.view;

import java.util.List;

import org.joda.time.Interval;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.etnetera.projects.testreporting.webapp.model.elasticsearch.result.Result;
import com.etnetera.projects.testreporting.webapp.model.mongodb.MongoAuditedModel;
import com.etnetera.projects.testreporting.webapp.model.mongodb.user.User;
import com.etnetera.projects.testreporting.webapp.utils.list.ListModifier;

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
	 * Unique not required key for API access.
	 * Is not ID so it can be changed if needed.
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
		// TODO get results but include results with update time in given interval only
		return null;
	}
	
}
