package com.etnetera.tremapp.model.form.user;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.etnetera.tremapp.model.mongodb.user.User;
import com.etnetera.tremapp.repository.mongodb.user.ManualUserRepository;

public class EmailCommandValidator implements Validator {

	private ManualUserRepository manualUserRepository;
	
	private User editedUser;
	
	public EmailCommandValidator(ManualUserRepository userRepository, User editedUser) {
		this.manualUserRepository = userRepository;
		this.editedUser = editedUser;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return EmailCommand.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email.validation.email.required");
		EmailCommand command = (EmailCommand) target;
		
		User userByEmail = manualUserRepository.findOneByEmail(command.getEmail());
		if (userByEmail != null && (editedUser != null || !userByEmail.getId().equals(editedUser.getId()))) {
			errors.rejectValue("email", "email.validation.email.unique");
		}
	} 

}
