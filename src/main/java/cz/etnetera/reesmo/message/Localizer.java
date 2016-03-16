package cz.etnetera.reesmo.message;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import cz.etnetera.reesmo.model.elasticsearch.result.TestSeverity;
import cz.etnetera.reesmo.model.elasticsearch.result.TestStatus;
import cz.etnetera.reesmo.model.mongodb.user.Permission;
import cz.etnetera.reesmo.user.UserType;

@Component
public class Localizer {

	@Autowired
	private MessageSource messageSource;
	
	public MessageSource getMessageSource() {
		return messageSource;
	}

	public String localize(boolean bool, Locale locale) {
		return messageSource.getMessage(bool ? "true" : "false", null, locale);
	}
	
	public String localize(UserType userType, Locale locale) {
		return userType == null ? null : messageSource.getMessage("user.type.value." + userType.name().toLowerCase(), null, locale);
	}
	
	public String localize(Permission permission, Locale locale) {
		return permission == null ? null : messageSource.getMessage("user.permission.value." + permission.name().toLowerCase(), null, locale);
	}
	
	public String localize(TestStatus status, Locale locale) {
		return status == null ? null : messageSource.getMessage("result.status.value." + status.name().toLowerCase(), null, locale);
	}
	
	public String localize(TestSeverity severity, Locale locale) {
		return severity == null ? null : messageSource.getMessage("result.severity.value." + severity.name().toLowerCase(), null, locale);
	}
	
}
