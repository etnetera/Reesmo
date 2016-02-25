package cz.etnetera.reesmo.model;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cz.etnetera.reesmo.user.UserManager;

@Component
public class ModelAuditor {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ModelAuditor.class);
	
	private static ModelAuditor instance;
	
	@Autowired
	private UserManager userManager;
	
	public static ModelAuditor getInstance() {
		return instance;
	}
	
	private ModelAuditor() {
		instance = this;
	}
	
	public void audit(AuditedModel model) {
		boolean create = model.getCreatedAt() == null;
		Date date = new Date();
		String userId = userManager.getUserId();
		
		if (create) {
			model.setCreatedAt(date);
			model.setCreatedBy(userId);
		}
		model.setUpdatedAt(date);
		model.setUpdatedBy(userId);
		
		LOGGER.debug("Model " + model.getClass().getSimpleName() + " was audited at " + date + " by " + userId + ".");
	}
	
}
