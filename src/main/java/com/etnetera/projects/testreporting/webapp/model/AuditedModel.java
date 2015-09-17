package com.etnetera.projects.testreporting.webapp.model;

import java.util.Date;

public interface AuditedModel {

	public String getCreatedBy();

	public void setCreatedBy(String createdBy);

	public String getUpdatedBy();

	public void setUpdatedBy(String updatedBy);

	public Date getCreatedAt();

	public void setCreatedAt(Date createdAt);

	public Date getUpdatedAt();

	public void setUpdatedAt(Date updatedAt);
	
}
