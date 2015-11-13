package com.etnetera.tremapp.model.form.user;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class UserProfileCommandValidator implements Validator {
	
	protected UsernameCommandValidator usernameValidator;
	
	protected EmailCommandValidator emailValidator;
	
	public UserProfileCommandValidator(UsernameCommandValidator usernameValidator, EmailCommandValidator emailValidator) {
		this.usernameValidator = usernameValidator;
		this.emailValidator = emailValidator;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return UserProfileCommand.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.invokeValidator(usernameValidator, target, errors);
		ValidationUtils.invokeValidator(emailValidator, target, errors);
	} 

}
