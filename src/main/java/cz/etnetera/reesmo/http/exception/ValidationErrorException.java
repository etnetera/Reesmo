package cz.etnetera.reesmo.http.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ValidationErrorException extends RuntimeException {

	public ValidationErrorException() {
		super();
	}

	public ValidationErrorException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ValidationErrorException(String message, Throwable cause) {
		super(message, cause);
	}

	public ValidationErrorException(String message) {
		super(message);
	}

	public ValidationErrorException(Throwable cause) {
		super(cause);
	}

}
