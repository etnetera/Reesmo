package com.etnetera.tremapp.model.mongodb.monitoring;

import org.joda.time.Interval;
import org.springframework.data.annotation.Id;

import com.etnetera.tremapp.model.mongodb.MongoAuditedModel;
import com.etnetera.tremapp.model.mongodb.view.View;

abstract public class Monitoring extends MongoAuditedModel {

	@Id
	private String id;
	
	private Interval updateInterval;
	
	private String view;

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

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}
	
	public void setView(View view) {
		this.view = view.getId();
	}
	
}
