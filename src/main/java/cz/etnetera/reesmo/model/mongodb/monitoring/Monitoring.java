package cz.etnetera.reesmo.model.mongodb.monitoring;

import cz.etnetera.reesmo.model.mongodb.MongoAuditedModel;
import cz.etnetera.reesmo.model.mongodb.view.View;
import org.joda.time.Interval;
import org.springframework.data.annotation.Id;

abstract public class Monitoring extends MongoAuditedModel {

	@Id
	private String id;
	
	private Interval updateInterval;
	
	private boolean enabled;
	
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

	public String getViewId() {
		return viewId;
	}

	public void setViewId(String viewId) {
		this.viewId = viewId;
	}
	
	public void setView(View view) {
		this.viewId = view.getId();
	}
	
}
