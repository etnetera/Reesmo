package cz.etnetera.reesmo.model.elasticsearch.result;

import cz.etnetera.reesmo.model.elasticsearch.ElasticAuditedModel;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

public class ResultAttachment extends ElasticAuditedModel {

	/**
     * Reference id under which this file is stored in MongoDB GridFS.
     */
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
    private String id;
	
	@Field(type = FieldType.String)
	private String name;
	
	@Field(type = FieldType.String)
	private String path;
	
	@Field(type = FieldType.String)
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
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
