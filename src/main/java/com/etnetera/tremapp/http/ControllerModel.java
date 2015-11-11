package com.etnetera.tremapp.http;

import com.etnetera.tremapp.http.exception.NotFoundException;

public class ControllerModel {

	public static void exists(Object model, Class<?> modelClass) {
		if (model == null) {
			throw new NotFoundException(modelClass.getSimpleName() + " does not exists!");
		}
		if (!modelClass.isAssignableFrom(model.getClass())) {
			throw new IllegalArgumentException("Model must be instance of " + modelClass.getName() + ", is " + model.getClass().getName() + "!");
		}
	}
	
}
