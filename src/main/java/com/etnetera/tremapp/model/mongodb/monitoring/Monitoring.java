package com.etnetera.tremapp.model.mongodb.monitoring;

import org.joda.time.Interval;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;

import com.etnetera.tremapp.model.mongodb.view.View;

abstract public class Monitoring {

	@Id
	private String id;
	
	private Interval updateInterval;
	
	@DBRef
	private View view;

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

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}
	
}
