package com.etnetera.tremapp.web.model.elasticsearch;

import java.util.Date;

import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.etnetera.tremapp.web.model.AuditedModel;

abstract public class ElasticAuditedModel implements AuditedModel {
		
	private String createdBy;
	
	private String updatedBy;
	
	@Field(type = FieldType.Date, format = DateFormat.date_time)
	private Date createdAt;
	
	@Field(type = FieldType.Date, format = DateFormat.date_time)
	private Date updatedAt;

	@Override
	public String getCreatedBy() {
		return createdBy;
	}

	@Override
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Override
	public String getUpdatedBy() {
		return updatedBy;
	}

	@Override
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Override
	public Date getCreatedAt() {
		return createdAt;
	}

	@Override
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public Date getUpdatedAt() {
		return updatedAt;
	}

	@Override
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
	
}
