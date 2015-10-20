package com.etnetera.tremapp.model.mongodb.monitoring;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "monitoring")
public class URLMonitoring extends Monitoring {

	private List<String> urls;

	public List<String> getUrls() {
		return urls;
	}

	public void setUrls(List<String> urls) {
		this.urls = urls;
	}
	
}
