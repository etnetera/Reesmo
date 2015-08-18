package com.etnetera.projects.testreporting.webapp.model.mongodb.view;

import org.springframework.data.annotation.Id;

import com.etnetera.projects.testreporting.webapp.model.mongodb.MongoAuditedModel;

/**
 * Modifier representation. Could present as filter or sorter.
 * It will be applied on elastic search test repository. 
 * 
 * @author zdenek
 *
 */
abstract public class ViewModifier extends MongoAuditedModel {

	@Id
	private String id;
	
	/**
	 * TODO - Add parameter (elastic search test repository) to apply method - otherwise is useless.
	 */
	abstract public void apply();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
