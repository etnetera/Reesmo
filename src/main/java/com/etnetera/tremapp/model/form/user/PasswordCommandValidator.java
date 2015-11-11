package com.etnetera.tremapp.model.form.user;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class PasswordCommandValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return PasswordCommand.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.validation.password.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "passwordConfirm", "password.validation.passwordConfirm.required");
		
		PasswordCommand passwordCommand = (PasswordCommand) target;
		if (!passwordCommand.getPassword().equals(passwordCommand.getPasswordConfirm())) {
			errors.rejectValue("passwordConfirm", "password.validation.passwordConfirm.match");
		}
	}

}
