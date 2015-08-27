package com.etnetera.projects.testreporting.webapp.model.elasticsearch.suite;

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

@Document(indexName = "resultdata", type = "suite")
public class Suite extends ElasticAuditedModel {
	
	@Id
	private String id;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String projectId;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String suiteId;
	
	@Field(type = FieldType.String)
	private String name;
	
	@Field(type = FieldType.String)
	private String description;
	
	@Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date startedAt;
	
	@Field(type = FieldType.Date, format = DateFormat.custom, pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
	private Date endedAt;
	
	@Field(type = FieldType.Long)
	private Long duration;
	
	@Field(type = FieldType.Object)
	private List<String> resultIds = new ArrayList<>();
	
	@Field(type = FieldType.Object)
	private List<String> labels = new ArrayList<>();

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

	public List<String> getResultIds() {
		return resultIds;
	}

	public void setResultIds(List<String> resultIds) {
		this.resultIds = resultIds;
	}

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}
	
}
