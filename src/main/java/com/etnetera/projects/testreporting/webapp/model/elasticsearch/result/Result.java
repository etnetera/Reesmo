package com.etnetera.projects.testreporting.webapp.model.elasticsearch.result;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.etnetera.projects.testreporting.webapp.model.elasticsearch.ElasticAuditedModel;

@Document(indexName = "resultdata", type = "result")
public class Result extends ElasticAuditedModel {
	
	@Id
	private String id;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String projectId;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String suiteId;
	
	@Field(type = FieldType.String)
	private String milestone;
	
	@Field(type = FieldType.String)
	private String name;
	
	@Field(type = FieldType.String)
	private String description;
	
	@Field(type = FieldType.String)
	private String environment;
	
	@Field(type = FieldType.String)
	private String author;
	
	@Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date startedAt;
	
	@Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date endedAt;
	
	@Field(type = FieldType.Long)
	private Long duration;
	
	@Field(type = FieldType.String)
	private TestStatus status;
	
	@Field(type = FieldType.String)
	private TestSeverity severity;
	
	@Field(type = FieldType.Boolean)
	private boolean automated;
	
	@Field(type = FieldType.String)
	private List<String> labels = new ArrayList<>();
	
	@Field(type = FieldType.String)
	private List<TestCategory> categories = new ArrayList<>();
	
	@Field(type = FieldType.String)
	private List<TestType> types = new ArrayList<>();
	
	@Field(type = FieldType.Nested)
	private List<ResultAttachment> attachments = new ArrayList<>();
	
	@Field(type = FieldType.Nested)
	private List<ResultLink> links = new ArrayList<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getSuiteId() {
		return suiteId;
	}

	public void setSuiteId(String suiteId) {
		this.suiteId = suiteId;
	}

	public String getMilestone() {
		return milestone;
	}

	public void setMilestone(String milestone) {
		this.milestone = milestone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getStartedAt() {
		return startedAt;
	}

	public void setStartedAt(Date startedAt) {
		this.startedAt = startedAt;
	}

	public Date getEndedAt() {
		return endedAt;
	}

	public void setEndedAt(Date endedAt) {
		this.endedAt = endedAt;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public TestStatus getStatus() {
		return status;
	}

	public void setStatus(TestStatus status) {
		this.status = status;
	}

	public TestSeverity getSeverity() {
		return severity;
	}

	public void setSeverity(TestSeverity severity) {
		this.severity = severity;
	}

	public boolean isAutomated() {
		return automated;
	}

	public void setAutomated(boolean automated) {
		this.automated = automated;
	}

	public List<TestCategory> getCategories() {
		return categories;
	}

	public void setCategories(List<TestCategory> categories) {
		this.categories = categories;
	}

	public List<TestType> getTypes() {
		return types;
	}

	public void setTypes(List<TestType> types) {
		this.types = types;
	}

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

	public List<ResultAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<ResultAttachment> attachments) {
		this.attachments = attachments;
	}

	public List<ResultLink> getLinks() {
		return links;
	}

	public void setLinks(List<ResultLink> links) {
		this.links = links;
	}
	
	public ResultAttachment getAttachment(String attachmentId) {
		return attachments.stream().filter(a -> a.getId().equals(attachmentId)).findFirst().orElse(null);
	}
	
	public ResultAttachment addAttachment(ResultAttachment attachment) {
		attachments.add(attachment);
		return attachment;
	}
	
	public void removeAttachment(ResultAttachment attachment) {
		attachments.remove(attachment); 
	}
	
}
