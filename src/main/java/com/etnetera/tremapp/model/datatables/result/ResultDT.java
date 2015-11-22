package com.etnetera.tremapp.model.datatables.result;

import java.util.Date;
import java.util.Locale;

import com.etnetera.tremapp.message.Localizer;
import com.etnetera.tremapp.model.datatables.AuditedModelDT;
import com.etnetera.tremapp.model.elasticsearch.result.Result;

public class ResultDT extends AuditedModelDT {

	private String id;
	
	private String suite;
	
	private String name;
	
	private String author;
	
	private Date startedAt;
	
	private Date endedAt;
	
	private String status;
	
	private String severity;
	
	private String automated;
	
	private String projectId;
	
	public ResultDT(Result result, Localizer localizer, Locale locale) {
		super(result);
		this.id = result.getId();
		this.suite = result.getSuite();
		this.name = result.getName();
		this.author = result.getAuthor();
		this.startedAt = result.getStartedAt();
		this.endedAt = result.getEndedAt();
		this.status = localizer.localize(result.getStatus(), locale);
		this.severity = localizer.localize(result.getSeverity(), locale);
		this.automated = localizer.localize(result.isAutomated(), locale);
		this.projectId = result.getProjectId();
	}

	public String getId() {
		return id;
	}

	public String getSuite() {
		return suite;
	}

	public String getName() {
		return name;
	}

	public String getAuthor() {
		return author;
	}

	public Date getStartedAt() {
		return startedAt;
	}

	public Date getEndedAt() {
		return endedAt;
	}

	public String getStatus() {
		return status;
	}

	public String getSeverity() {
		return severity;
	}

	public String getAutomated() {
		return automated;
	}

	public String getProjectId() {
		return projectId;
	}

}
