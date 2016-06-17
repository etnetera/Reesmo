package cz.etnetera.reesmo.model.form.monitor;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class MonitorCommandValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return MonitorCommand.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {



    }
}
