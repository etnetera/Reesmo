package cz.etnetera.reesmo.model.mongodb.view;

import java.util.List;

import org.joda.time.Interval;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import cz.etnetera.reesmo.list.ListModifier;
import cz.etnetera.reesmo.model.elasticsearch.result.Result;
import cz.etnetera.reesmo.model.mongodb.MongoAuditedModel;

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

	private String name;

	private String description;

	private ListModifier modifier;
	
	private String projectId;

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

	public ListModifier getModifier() {
		return modifier;
	}

	public void setModifier(ListModifier modifier) {
		this.modifier = modifier;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
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

}
