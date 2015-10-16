package com.etnetera.tremapp.web.model.mongodb.project;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.etnetera.tremapp.web.model.mongodb.MongoAuditedModel;

@Document
public class Project extends MongoAuditedModel {

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
	
}
