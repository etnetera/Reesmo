package com.etnetera.projects.testreporting.webapp.model.elasticsearch.result;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.etnetera.projects.testreporting.webapp.model.elasticsearch.ElasticAuditedModel;

public class ResultAttachment extends ElasticAuditedModel {

	/**
     * Reference id under which this file is stored in MongoDB GridFS.
     */
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String id;
	
	@Field(type = FieldType.String)
	private String name;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String contentType;
	
	@Field(type = FieldType.Long)
	private long size;

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

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}
	
}
