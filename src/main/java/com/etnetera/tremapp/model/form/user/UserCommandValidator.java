package com.etnetera.tremapp.model.form.user;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import com.etnetera.tremapp.model.mongodb.user.User;

public class UserCommandValidator extends UserProfileCommandValidator {

	private PasswordCommandValidator passwordValidator;
	
	private User editedUser;
	
	public UserCommandValidator(UsernameCommandValidator usernameValidator, EmailCommandValidator emailValidator, PasswordCommandValidator passwordValidator, User editedUser) {
		super(usernameValidator, emailValidator);
		this.passwordValidator = passwordValidator;
		this.editedUser = editedUser;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return UserCommand.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		super.validate(target, errors);
		if (editedUser == null) {
			ValidationUtils.invokeValidator(passwordValidator, target, errors);
		}
	} 

}
