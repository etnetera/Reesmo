package com.etnetera.tremapp.model.mongodb.monitoring;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class EmailMonitoring extends Monitoring {

	private List<String> emails;

	public List<String> getEmails() {
		return emails;
	}

	public void setEmails(List<String> emails) {
		this.emails = emails;
	}
	
}
