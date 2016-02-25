package cz.etnetera.reesmo.model.form.project;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import cz.etnetera.reesmo.model.mongodb.project.Project;
import cz.etnetera.reesmo.repository.mongodb.project.ProjectRepository;

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
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "key", "validator.NotEmpty.message");
		ProjectCommand command = (ProjectCommand) target;
		
		Project projectByKey = projectRepository.findOneByKey(command.getKey());
		if (projectByKey != null && (editedProject == null || !projectByKey.getId().equals(editedProject.getId()))) {
			errors.rejectValue("key", "validator.Unique.message");
		}
	} 

}
