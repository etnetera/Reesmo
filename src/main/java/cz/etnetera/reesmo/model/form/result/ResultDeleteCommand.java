package cz.etnetera.reesmo.model.form.result;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

public class ResultDeleteCommand {
	
	@NotEmpty
	private List<String> resultIds;

	public List<String> getResultIds() {
		return resultIds;
	}

	public void setResultIds(List<String> resultIds) {
		this.resultIds = resultIds;
	}

}
