package cz.etnetera.reesmo.model.mongodb.monitoring;

import cz.etnetera.reesmo.model.mongodb.MongoAuditedModel;
import cz.etnetera.reesmo.model.mongodb.view.View;
import cz.etnetera.reesmo.notifier.Notifier2;
import org.joda.time.Interval;
import org.springframework.data.annotation.Id;

import java.util.List;

abstract public class Monitoring extends MongoAuditedModel {

	@Id
	private String id;
	
	private Interval updateInterval;
	
	private boolean enabled;
	
	private String viewId;

	private List<Notifier2> notifiers;

	public List<Notifier2> getNotifiers() {
		return notifiers;
	}

	public void setNotifiers(List<Notifier2> notifiers) {
		this.notifiers = notifiers;
	}

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
