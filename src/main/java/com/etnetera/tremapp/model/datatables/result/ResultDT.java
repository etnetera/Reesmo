package com.etnetera.tremapp.model.datatables.result;

import java.util.Locale;

import com.etnetera.tremapp.message.Localizer;
import com.etnetera.tremapp.model.datatables.AuditedModelDT;
import com.etnetera.tremapp.model.elasticsearch.result.Result;

public class ResultDT extends AuditedModelDT {

	private String id;
	
	private String suite;
	
	private String name;
	
	private String author;
	
	private String startedAt;
	
	private String endedAt;
	
	private Long length;
	
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
		this.startedAt = result.getStartedAt() == null ? null : result.getStartedAt().toString();
		this.endedAt = result.getEndedAt() == null ? null : result.getEndedAt().toString();
		if (result.getStartedAt() != null && result.getEndedAt() != null) {
			this.length = result.getEndedAt().getTime() - result.getStartedAt().getTime();
		}
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

	public String getStartedAt() {
		return startedAt;
	}

	public String getEndedAt() {
		return endedAt;
	}

	public Long getLength() {
		return length;
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
