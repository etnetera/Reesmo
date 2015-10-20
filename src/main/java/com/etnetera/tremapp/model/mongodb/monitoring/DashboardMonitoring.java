package com.etnetera.tremapp.model.mongodb.monitoring;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "monitoring")
public class DashboardMonitoring extends Monitoring {

	private String position;
	
	private boolean collapsed;

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public boolean isCollapsed() {
		return collapsed;
	}

	public void setCollapsed(boolean collapsed) {
		this.collapsed = collapsed;
	}
	
}
