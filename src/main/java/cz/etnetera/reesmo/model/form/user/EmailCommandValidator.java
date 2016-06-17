package cz.etnetera.reesmo.model.form.user;

import cz.etnetera.reesmo.model.mongodb.user.User;
import cz.etnetera.reesmo.repository.mongodb.user.ManualUserRepository;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

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
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "validator.NotEmpty.message");
		EmailCommand command = (EmailCommand) target;
		
		User userByEmail = manualUserRepository.findOneByEmail(command.getEmail());
		if (userByEmail != null && (editedUser == null || !userByEmail.getId().equals(editedUser.getId()))) {
			errors.rejectValue("email", "validator.Unique.message");
		}
	} 

}
