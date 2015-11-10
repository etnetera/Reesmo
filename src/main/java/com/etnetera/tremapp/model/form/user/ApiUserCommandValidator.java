package com.etnetera.tremapp.model.form.user;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.etnetera.tremapp.repository.mongodb.user.ApiUserRepository;

public class ApiUserCommandValidator implements Validator {

	private ApiUserRepository apiUserRepository;
	
	public ApiUserCommandValidator(ApiUserRepository apiUserRepository) {
		this.apiUserRepository = apiUserRepository;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return ApiUserCommandInterface.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ApiUserCommandInterface userCommand = (ApiUserCommandInterface) target;
	}

}
