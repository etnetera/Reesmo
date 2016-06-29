package cz.etnetera.reesmo.model.mongodb.monitoring;

import cz.etnetera.reesmo.model.mongodb.MongoAuditedModel;
import org.springframework.data.annotation.Id;

abstract public class Monitoring extends MongoAuditedModel {

	@Id
	protected String id;

	protected boolean enabled;

	protected String projectId;

	protected String viewId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getViewId() {
		return viewId;
	}

	public void setViewId(String viewId) {
		this.viewId = viewId;
	}
	
}
