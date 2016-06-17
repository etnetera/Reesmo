package cz.etnetera.reesmo.model.mongodb.resultchange;

import cz.etnetera.reesmo.model.mongodb.MongoAuditedModel;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Stores info about change in record.
 * We store changes for CREATE, UPDATE and DELETE.
 * 
 * @author zdenek
 * 
 */
@Document
public class ResultChange extends MongoAuditedModel {

	@Id
	private String id;

	private String resultId;

	private ResultChangeAction action;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getResultId() {
		return resultId;
	}

	public void setResultId(String resultId) {
		this.resultId = resultId;
	}

	public ResultChangeAction getAction() {
		return action;
	}

	public void setAction(ResultChangeAction action) {
		this.action = action;
	}

}
