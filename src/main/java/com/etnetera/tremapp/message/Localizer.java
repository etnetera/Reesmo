package com.etnetera.tremapp.message;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import com.etnetera.tremapp.model.mongodb.user.Permission;
import com.etnetera.tremapp.user.UserType;

@Component
public class Localizer {

	@Autowired
	private MessageSource messageSource;
	
	public String localize(UserType userType, Locale locale) {
		return messageSource.getMessage("user.type.value." + userType.name().toLowerCase(), null, locale);
	}
	
	public String localize(Permission permission, Locale locale) {
		return messageSource.getMessage("member.permission.value." + permission.name().toLowerCase(), null, locale);
	}
	
	public String localize(boolean bool, Locale locale) {
		return messageSource.getMessage(bool ? "true" : "false", null, locale);
	}
	
}
