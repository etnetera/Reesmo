package cz.etnetera.reesmo.model.mongodb.monitoring;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "monitoring")
public class EmailMonitoring extends Monitoring {

	private List<String> emails = new ArrayList<>();

	public List<String> getEmails() {
		return emails;
	}

	public void setEmails(List<String> emails) {
		this.emails = emails;
	}
	
}
