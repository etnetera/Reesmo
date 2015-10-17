package com.etnetera.tremapp.model.elasticsearch.result;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.etnetera.tremapp.model.elasticsearch.ElasticAuditedModel;

public class ResultLink extends ElasticAuditedModel {
	
	@Field(type = FieldType.String)
	private String name;
	
	@Field(type = FieldType.String, index = FieldIndex.not_analyzed)
	private String url;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
