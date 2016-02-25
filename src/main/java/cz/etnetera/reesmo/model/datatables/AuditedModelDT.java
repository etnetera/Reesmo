package cz.etnetera.reesmo.model.datatables;

import java.util.Date;

import cz.etnetera.reesmo.model.AuditedModel;

abstract public class AuditedModelDT {
	
	private String createdBy;
	
	private String updatedBy;
	
	private Date createdAt;
	
	private Date updatedAt;

	public AuditedModelDT(AuditedModel model) {
		this.createdBy = model.getCreatedBy();
		this.updatedBy = model.getUpdatedBy();
		this.createdAt = model.getCreatedAt();
		this.updatedAt = model.getUpdatedAt();
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}
	
}
