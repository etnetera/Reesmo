package cz.etnetera.reesmo.model.form.user;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import cz.etnetera.reesmo.model.mongodb.user.User;
import cz.etnetera.reesmo.user.UserType;

public class UserCommandValidator implements Validator {

	protected UsernameCommandValidator usernameValidator;
	
	protected EmailCommandValidator emailValidator;
	
	protected PasswordCommandValidator passwordValidator;
	
	protected User editedUser;
	
	public UserCommandValidator(UsernameCommandValidator usernameValidator, EmailCommandValidator emailValidator, PasswordCommandValidator passwordValidator, User editedUser) {
		this.usernameValidator = usernameValidator;
		this.emailValidator = emailValidator;
		this.passwordValidator = passwordValidator;
		this.editedUser = editedUser;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return UserCommand.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		UserCommand command = (UserCommand) target;
		ValidationUtils.invokeValidator(usernameValidator, target, errors);
		if (UserType.MANUAL.is(command.getType())) {
			ValidationUtils.invokeValidator(emailValidator, target, errors);
		}
		if (editedUser == null) {
			ValidationUtils.invokeValidator(passwordValidator, target, errors);
		}
	} 

}
