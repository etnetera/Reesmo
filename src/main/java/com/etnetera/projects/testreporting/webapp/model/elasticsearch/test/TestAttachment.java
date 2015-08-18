package com.etnetera.projects.testreporting.webapp.model.elasticsearch.test;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.etnetera.projects.testreporting.webapp.model.elasticsearch.ElasticAuditedModel;

@Document(indexName = "testresultdata", type = "testattachment")
public class TestAttachment extends ElasticAuditedModel {

	@Id
    private String id;
	
	@Field(type = FieldType.String)
	private String name;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String path;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
}
