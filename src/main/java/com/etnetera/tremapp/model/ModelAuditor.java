package com.etnetera.tremapp.model;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.etnetera.tremapp.user.UserHelper;

public class ModelAuditor {

	private static final Logger LOGGER = LoggerFactory.getLogger(ModelAuditor.class);
	
	public static void audit(AuditedModel model) {
		boolean create = model.getCreatedAt() == null;
		Date date = new Date();
		String userId = UserHelper.getUserId();
		
		if (create) {
			model.setCreatedAt(date);
			model.setCreatedBy(userId);
		}
		model.setUpdatedAt(date);
		model.setUpdatedBy(userId);
		
		LOGGER.debug("Model " + model.getClass().getSimpleName() + " was audited at " + date + " by " + userId + ".");
	}
	
}
