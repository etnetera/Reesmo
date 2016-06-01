package cz.etnetera.reesmo.model.form.view;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ViewCommandValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return ViewCommand.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
	}

}
