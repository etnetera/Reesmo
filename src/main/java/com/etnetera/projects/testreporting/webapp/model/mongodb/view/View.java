package com.etnetera.projects.testreporting.webapp.model.mongodb.view;

import java.util.List;

import org.joda.time.Interval;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.etnetera.projects.testreporting.webapp.model.elasticsearch.test.TestResult;
import com.etnetera.projects.testreporting.webapp.model.mongodb.MongoAuditedModel;
import com.etnetera.projects.testreporting.webapp.model.mongodb.user.User;

/**
 * Describes stored view representing test list.
 * List can be filtered, with offset and limited.
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
	
	private Long offset;
	
	private Long limit;
	
	private List<ViewModifier> modifiers;
	
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

	public Long getOffset() {
		return offset;
	}

	public void setOffset(Long offset) {
		this.offset = offset;
	}

	public Long getLimit() {
		return limit;
	}

	public void setLimit(Long limit) {
		this.limit = limit;
	}

	public List<ViewModifier> getModifiers() {
		return modifiers;
	}

	public void setModifiers(List<ViewModifier> modifiers) {
		this.modifiers = modifiers;
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

	public List<TestResult> getResults() {
		// TODO apply all modifiers and limit
		return null;
	}

	public List<TestResult> getChangedResults(Interval interval) {
		// TODO get results but include results with update time in given interval only
		return null;
	}
	
}
