package com.etnetera.tremapp.model.form.user;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.etnetera.tremapp.model.mongodb.user.User;
import com.etnetera.tremapp.repository.mongodb.user.UserRepository;

public class UsernameCommandValidator implements Validator {

	private UserRepository userRepository;
	
	private User editedUser;
	
	public UsernameCommandValidator(UserRepository userRepository, User editedUser) {
		this.userRepository = userRepository;
		this.editedUser = editedUser;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return UsernameCommand.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "validator.NotEmpty.message");
		UsernameCommand command = (UsernameCommand) target;
		
		User userByUsername = userRepository.findOneByUsername(command.getUsername());
		if (userByUsername != null && (editedUser == null || !userByUsername.getId().equals(editedUser.getId()))) {
			errors.rejectValue("username", "validator.Unique.message");
		}
	} 

}
