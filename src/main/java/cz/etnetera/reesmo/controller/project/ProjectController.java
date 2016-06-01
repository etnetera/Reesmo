package cz.etnetera.reesmo.controller.project;

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

import cz.etnetera.reesmo.controller.MenuActivityController;
import cz.etnetera.reesmo.http.ControllerModel;
import cz.etnetera.reesmo.model.form.project.ProjectCommand;
import cz.etnetera.reesmo.model.form.project.ProjectCommandValidator;
import cz.etnetera.reesmo.model.mongodb.project.Project;
import cz.etnetera.reesmo.model.mongodb.user.Permission;
import cz.etnetera.reesmo.repository.mongodb.project.ProjectRepository;
import cz.etnetera.reesmo.user.UserManager;

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
	
	@RequestMapping(value = "/project/home/{projectId}", method = RequestMethod.GET)
	public String projectHome(@PathVariable String projectId) {
		return "redirect:/project/results/" + projectId;
	}
	
	@RequestMapping(value = "/project/detail/{projectId}", method = RequestMethod.GET)
	public String showProject(@PathVariable String projectId, Model model) {
		Project project = projectRepository.findOne(projectId);
		ControllerModel.exists(project, Project.class);
		project.checkUserPermission(userManager.requireUser(), Permission.BASIC);
		model.addAttribute("project", project);
		return "page/project/projectDetail";
	}

	@RequestMapping(value = "/project/edit/{projectId}", method = RequestMethod.GET)
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

	@RequestMapping(value = "/project/edit/{projectId}", method = RequestMethod.POST)
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
		return "redirect:/project/detail/" + project.getId();
	}

	@RequestMapping(value = "/project/delete/{projectId}", method = RequestMethod.GET)
	public String deleteProject(@PathVariable String projectId, Model model) {
		Project project = projectRepository.findOne(projectId);
		ControllerModel.exists(project, Project.class);
		project.checkUserPermission(userManager.requireUser(), Permission.OWNER);
		model.addAttribute("project", project);
		return "page/project/projectDelete";
	}

	@RequestMapping(value = "/project/delete/{projectId}", method = RequestMethod.POST)
	public String deleteProject(@PathVariable String projectId) {
		Project project = projectRepository.findOne(projectId);
		ControllerModel.exists(project, Project.class);
		project.checkUserPermission(userManager.requireUser(), Permission.OWNER);
		projectRepository.deleteProject(project);
		return "redirect:/projects";
	}

}
