package cz.etnetera.reesmo.model.datatables.result;

import java.util.Locale;

import cz.etnetera.reesmo.message.Localizer;
import cz.etnetera.reesmo.model.datatables.AuditedModelDT;
import cz.etnetera.reesmo.model.elasticsearch.result.Result;
import cz.etnetera.reesmo.model.mongodb.project.Project;

public class ResultDT extends AuditedModelDT {

	private String id;
	
	private String suite;
	
	private String name;
	
	private String author;
	
	private String startedAt;
	
	private String endedAt;
	
	private String length;
	
	private String status;
	
	private String statusValue;
	
	private String severity;
	
	private String severityValue;
	
	private String automated;
	
	private String projectId;
	
	private String projectName;
	
	public ResultDT(Result result, Project project, Localizer localizer, Locale locale) {
		super(result);
		this.id = result.getId();
		this.suite = result.getSuite();
		this.name = result.getName();
		this.author = result.getAuthor();
		this.startedAt = result.getStartedAt() == null ? null : result.getStartedAt().toString();
		this.endedAt = result.getEndedAt() == null ? null : result.getEndedAt().toString();
		if (result.getStartedAt() != null && result.getEndedAt() != null) {
			this.length = String.valueOf(result.getEndedAt().getTime() - result.getStartedAt().getTime()) + " ms";
		}
		this.status = localizer.localize(result.getStatus(), locale);
		this.statusValue = result.getStatus() == null ? null : result.getStatus().name();
		this.severity = localizer.localize(result.getSeverity(), locale);
		this.severityValue = result.getSeverity() == null ? null : result.getSeverity().name();
		this.automated = localizer.localize(result.isAutomated(), locale);
		this.projectId = result.getProjectId();
		this.projectName = project == null ? null : project.getName();
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

	public String getLength() {
		return length;
	}

	public String getStatus() {
		return status;
	}

	public String getStatusValue() {
		return statusValue;
	}

	public String getSeverity() {
		return severity;
	}

	public String getSeverityValue() {
		return severityValue;
	}

	public String getAutomated() {
		return automated;
	}

	public String getProjectId() {
		return projectId;
	}

	public String getProjectName() {
		return projectName;
	}

}
