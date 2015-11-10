package com.etnetera.tremapp.model.form.user;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.etnetera.tremapp.repository.mongodb.user.ManualUserRepository;

public class ManualUserCommandValidator implements Validator {

	private ManualUserRepository manualUserRepository;
	
	public ManualUserCommandValidator(ManualUserRepository manualUserRepository) {
		this.manualUserRepository = manualUserRepository;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return ManualUserCommandInterface.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ManualUserCommandInterface userCommand = (ManualUserCommandInterface) target;
		if (manualUserRepository.findOneByEmail(userCommand.getEmail()) != null) {
			errors.rejectValue("email", "user.validation.email.unique", "E-mail is already taken by another user!");
		}
	}

}
