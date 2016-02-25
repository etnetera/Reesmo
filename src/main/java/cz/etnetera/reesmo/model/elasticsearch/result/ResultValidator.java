package cz.etnetera.reesmo.model.elasticsearch.result;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class ResultValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Result.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Result result = (Result) target;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "validator.NotEmpty.message");
		ValidationUtils.rejectIfEmpty(errors, "status", "validator.NotEmpty.message");
		ValidationUtils.rejectIfEmpty(errors, "severity", "validator.NotEmpty.message");
		
		ValidationUtils.rejectIfEmpty(errors, "startedAt", "validator.NotEmpty.message");
		if (result.getEndedAt() == null && result.getLength() == null) {
			ValidationUtils.rejectIfEmpty(errors, "endedAt", "validator.NotEmpty.result.endedAt.message");
		}
		if (result.getLength() != null && result.getLength().compareTo(new Long(0)) < 0) {
			errors.rejectValue("length", "validator.NoTNegative.message");
		}
		if (result.getStartedAt() != null && result.getEndedAt() != null && result.getStartedAt().after(result.getEndedAt())) {
			errors.rejectValue("endedAt", "validator.GreateOrEqual.startedAt.message");
		}
		
		if (result.getLinks() != null) {
			for (int i = 0; i < result.getLinks().size(); i++) {
				ResultLink link = result.getLinks().get(i);
				ValidationUtils.rejectIfEmpty(errors, "links[" + i + "]", "validator.NotEmpty.message");
				if (link == null) continue;
				ValidationUtils.rejectIfEmpty(errors, "links[" + i + "].url", "validator.NotEmpty.message");
				if (!StringUtils.isEmpty(link.getUrl())) {
					try {
						new URL(link.getUrl());
					} catch (MalformedURLException e) {
						errors.rejectValue("links[" + i + "].url", "validator.URL.message");
					}					
				}
			}
		}
	} 

}
