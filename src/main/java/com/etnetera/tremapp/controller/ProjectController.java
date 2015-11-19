package com.etnetera.tremapp.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.etnetera.tremapp.http.ControllerModel;
import com.etnetera.tremapp.model.form.project.ProjectCommand;
import com.etnetera.tremapp.model.form.project.ProjectCommandValidator;
import com.etnetera.tremapp.model.mongodb.project.Project;
import com.etnetera.tremapp.model.mongodb.user.Permission;
import com.etnetera.tremapp.repository.mongodb.project.ProjectRepository;
import com.etnetera.tremapp.user.UserManager;

@Controller
public class ProjectController implements MenuActivityController {
	
	@Autowired
    private UserManager userManager;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Override
	public String getActiveMenu() {
		return "projectSettings";
	}
	
	@InitBinder(value = "projectCommand")
	protected void initBinder(WebDataBinder binder, @PathVariable Optional<String> projectId) {		
		Project project = null;
		if (projectId.isPresent()) {
			project = projectRepository.findOne(projectId.get());
		}
		binder.addValidators(new ProjectCommandValidator(projectRepository, project));
	}
	
	@RequestMapping(value = "/projects/detail/{projectId}", method = RequestMethod.GET)
	public String showProject(@PathVariable String projectId, Model model) {
		Project project = projectRepository.findOne(projectId);
		ControllerModel.exists(project, Project.class);
		project.checkUserPermission(userManager.requireUser(), Permission.BASIC);
		model.addAttribute("project", project);
		return "page/project/projectDetail";
	}

	@RequestMapping(value = "/projects/edit/{projectId}", method = RequestMethod.GET)
	public String editProject(@PathVariable String projectId, Model model) {
		Project project = projectRepository.findOne(projectId);
		ControllerModel.exists(project, Project.class);
		project.checkUserPermission(userManager.requireUser(), Permission.ADMIN);
		ProjectCommand projectCommand = new ProjectCommand();
		projectCommand.fromProject(project);
		model.addAttribute("project", project);
		model.addAttribute("projectCommand", projectCommand);
		return "page/project/projectEdit";
	}

	@RequestMapping(value = "/projects/edit/{projectId}", method = RequestMethod.POST)
	public String editProject(@Valid ProjectCommand projectCommand,
			BindingResult bindingResult, @PathVariable String projectId, Model model) {
		Project project = projectRepository.findOne(projectId);
		ControllerModel.exists(project, Project.class);
		project.checkUserPermission(userManager.requireUser(), Permission.ADMIN);
		if (bindingResult.hasErrors()) {
			model.addAttribute("project", project);
			return "page/project/projectEdit";
		}
		projectCommand.toProject(project);
		projectRepository.save(project);
		return "redirect:/projects/detail/" + project.getId();
	}

	@RequestMapping(value = "/projects/delete/{projectId}", method = RequestMethod.GET)
	public String deleteProject(@PathVariable String projectId, Model model) {
		Project project = projectRepository.findOne(projectId);
		ControllerModel.exists(project, Project.class);
		project.checkUserPermission(userManager.requireUser(), Permission.OWNER);
		model.addAttribute("project", project);
		return "page/project/projectDelete";
	}

	@RequestMapping(value = "/projects/delete/{projectId}", method = RequestMethod.POST)
	public String deleteProject(@PathVariable String projectId) {
		Project project = projectRepository.findOne(projectId);
		ControllerModel.exists(project, Project.class);
		project.checkUserPermission(userManager.requireUser(), Permission.OWNER);
		projectRepository.delete(project);
		return "redirect:/projects";
	}

}
