package cz.etnetera.reesmo.model.mongodb.monitoring;

import cz.etnetera.reesmo.model.mongodb.MongoAuditedModel;
import org.joda.time.Interval;
import org.springframework.data.annotation.Id;

abstract public class Monitoring extends MongoAuditedModel {

	@Id
	private String id;
	
	private Interval updateInterval;
	
	private boolean enabled;

	private String projectId;

	private String viewId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Interval getUpdateInterval() {
		return updateInterval;
	}

	public void setUpdateInterval(Interval updateInterval) {
		this.updateInterval = updateInterval;
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
