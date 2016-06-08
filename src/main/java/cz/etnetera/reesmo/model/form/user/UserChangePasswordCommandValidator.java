package cz.etnetera.reesmo.model.form.user;

import cz.etnetera.reesmo.model.mongodb.user.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class UserChangePasswordCommandValidator implements Validator {
	
	protected PasswordCommandValidator passwordValidator;
	
	protected User sameLoggedUser;
	
	public UserChangePasswordCommandValidator(PasswordCommandValidator passwordValidator, User sameLoggedUser) {
		this.passwordValidator = passwordValidator;
		this.sameLoggedUser = sameLoggedUser;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return UserChangePasswordCommand.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if (sameLoggedUser != null) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "originalPassword", "validator.NotEmpty.message");
			
			UserChangePasswordCommand command = (UserChangePasswordCommand) target;
			BCryptPasswordEncoder passEncoder = new BCryptPasswordEncoder();
			if (!passEncoder.matches(command.getOriginalPassword(), sameLoggedUser.getPassword())) {
				errors.rejectValue("originalPassword", "validator.Mismatch.originalPassword.message");
			}
		}
		ValidationUtils.invokeValidator(passwordValidator, target, errors);
	} 

}
