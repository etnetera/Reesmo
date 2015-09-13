package com.etnetera.projects.testreporting.webapp.model.elasticsearch.result;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.etnetera.projects.testreporting.webapp.model.elasticsearch.ElasticAuditedModel;

@Document(indexName = "resultdata", type = "resultattachment")
public class ResultAttachment extends ElasticAuditedModel {

	@Id
    private String id;
	
	@Field(type = FieldType.String)
	private String name;
	
	/**
     * Reference id under which this file is stored in MongoDB GridFS.
     */
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String fileId;
	
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

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
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
