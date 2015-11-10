package com.etnetera.tremapp.model.form.user;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.etnetera.tremapp.repository.mongodb.user.UserRepository;

public class UserCommandValidator implements Validator {

	private UserRepository userRepository;
	
	public UserCommandValidator(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return UserCommand.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		UserCommand userCommand = (UserCommand) target;
		if (userRepository.findOneByUsername(userCommand.getUsername()) != null) {
			errors.rejectValue("username", "user.validation.username.unique", "Username is already taken by another user!");
		}
	}

}
