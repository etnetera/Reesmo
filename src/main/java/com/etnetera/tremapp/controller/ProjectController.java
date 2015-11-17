package com.etnetera.tremapp.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.etnetera.tremapp.http.ControllerModel;
import com.etnetera.tremapp.model.datatables.project.ProjectDT;
import com.etnetera.tremapp.model.form.project.ProjectCommand;
import com.etnetera.tremapp.model.form.project.ProjectCommandValidator;
import com.etnetera.tremapp.model.mongodb.project.Project;
import com.etnetera.tremapp.model.mongodb.user.Permission;
import com.etnetera.tremapp.repository.mongodb.project.ProjectRepository;
import com.etnetera.tremapp.user.UserManager;
import com.etnetera.tremapp.user.UserRole;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;

@Controller
public class ProjectController implements MenuActivityController {
	
	@Autowired
    private UserManager userManager;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Override
	public String getActiveMenu() {
		return "projects";
	}
	
	@InitBinder(value = "projectCommand")
	protected void initBinder(WebDataBinder binder, @PathVariable Optional<String> projectId) {		
		Project project = null;
		if (projectId.isPresent()) {
			project = projectRepository.findOne(projectId.get());
		}
		binder.addValidators(new ProjectCommandValidator(projectRepository, project));
	}
	
	@RequestMapping(value = "/projects", method = RequestMethod.GET)
	public String projects() {
		return "page/project/projects";
	}

	@RequestMapping(value = "/dt/projects")
	public @ResponseBody DatatablesResponse<ProjectDT> findAllForDataTables(HttpServletRequest request) {
		DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
		DataSet<ProjectDT> projects = projectRepository.findWithDatatablesCriterias(criterias, userManager.getAllowedProjectIds(Permission.BASIC));
		return DatatablesResponse.build(projects, criterias);
	}
	
	@RequestMapping(value = "/projects/detail/{projectId}", method = RequestMethod.GET)
	public String showProject(@PathVariable String projectId, Model model) {
		Project project = projectRepository.findOne(projectId);
		ControllerModel.exists(project, Project.class);
		userManager.checkProjectPermission(projectId, Permission.BASIC);
		model.addAttribute("project", project);
		return "page/project/projectDetail";
	}

	@Secured({UserRole.ROLE_ADMIN})
	@RequestMapping(value = "/projects/edit/{projectId}", method = RequestMethod.GET)
	public String editProject(@PathVariable String projectId, Model model) {
		Project project = projectRepository.findOne(projectId);
		ControllerModel.exists(project, Project.class);
		ProjectCommand projectCommand = new ProjectCommand();
		projectCommand.fromProject(project);
		model.addAttribute("project", project);
		model.addAttribute("projectCommand", projectCommand);
		return "page/project/projectEdit";
	}

	@Secured({UserRole.ROLE_ADMIN})
	@RequestMapping(value = "/projects/edit/{projectId}", method = RequestMethod.POST)
	public String editProject(@Valid ProjectCommand projectCommand,
			BindingResult bindingResult, @PathVariable String projectId, Model model) {
		Project project = projectRepository.findOne(projectId);
		ControllerModel.exists(project, Project.class);
		if (bindingResult.hasErrors()) {
			model.addAttribute("project", project);
			return "page/project/projectEdit";
		}
		projectCommand.toProject(project);
		projectRepository.save(project);
		return "redirect:/projects/detail/" + project.getId();
	}

	@Secured({UserRole.ROLE_ADMIN})
	@RequestMapping(value = "/projects/create", method = RequestMethod.GET)
	public String createProject(Model model) {
		ProjectCommand projectCommand = new ProjectCommand();
		model.addAttribute("projectCommand", projectCommand);
		return "page/project/projectCreate";
	}

	@Secured({UserRole.ROLE_ADMIN})
	@RequestMapping(value = "/projects/create", method = RequestMethod.POST)
	public String createProject(@Valid ProjectCommand projectCommand, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "page/project/projectCreate";
		}
		Project project = new Project();
		projectCommand.toProject(project);
		projectRepository.save(project);
		return "redirect:/projects/detail/" + project.getId();
	}

	@Secured({UserRole.ROLE_ADMIN})
	@RequestMapping(value = "/projects/delete/{projectId}", method = RequestMethod.GET)
	public String deleteProject(@PathVariable String projectId, Model model) {
		Project project = projectRepository.findOne(projectId);
		ControllerModel.exists(project, Project.class);
		model.addAttribute("project", project);
		return "page/project/projectDelete";
	}

	@Secured({UserRole.ROLE_ADMIN})
	@RequestMapping(value = "/projects/delete/{projectId}", method = RequestMethod.POST)
	public String deleteProject(@PathVariable String projectId) {
		Project project = projectRepository.findOne(projectId);
		ControllerModel.exists(project, Project.class);
		projectRepository.delete(project);
		return "redirect:/projects";
	}

}
