package com.etnetera.tremapp.model.form.project;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.etnetera.tremapp.model.mongodb.project.Project;
import com.etnetera.tremapp.repository.mongodb.project.ProjectRepository;

public class ProjectCommandValidator implements Validator {

	private ProjectRepository projectRepository;
	
	private Project editedProject;
	
	public ProjectCommandValidator(ProjectRepository projectRepository, Project editedProject) {
		this.projectRepository = projectRepository;
		this.editedProject = editedProject;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return ProjectCommand.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ProjectCommand command = (ProjectCommand) target;
		
		if (StringUtils.isNotEmpty(command.getKey())) {
			Project projectByKey = projectRepository.findOneByKey(command.getKey());
			if (projectByKey != null && (editedProject == null || !projectByKey.getId().equals(editedProject.getId()))) {
				errors.rejectValue("key", "validator.Unique.message");
			}
		}
	} 

}
