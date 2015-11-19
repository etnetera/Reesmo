package com.etnetera.tremapp.controller;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.etnetera.tremapp.model.datatables.project.ProjectDT;
import com.etnetera.tremapp.model.form.project.ProjectCommand;
import com.etnetera.tremapp.model.form.project.ProjectCommandValidator;
import com.etnetera.tremapp.model.mongodb.project.Project;
import com.etnetera.tremapp.model.mongodb.user.Permission;
import com.etnetera.tremapp.repository.mongodb.project.ProjectRepository;
import com.etnetera.tremapp.user.UserManager;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;

@Controller
public class ProjectsController implements MenuActivityController {
	
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
	public @ResponseBody DatatablesResponse<ProjectDT> findAllForDataTables(@RequestParam(required = false, defaultValue = "BASIC") String permission, HttpServletRequest request) {
		Permission perm = Permission.fromString(permission);
		if (perm == null) {
			throw new IllegalArgumentException("Uknown permission " + permission);
		}
		DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
		DataSet<ProjectDT> projects = projectRepository.findWithDatatablesCriterias(criterias, userManager.getAllowedProjectIds(perm));
		return DatatablesResponse.build(projects, criterias);
	}

	@RequestMapping(value = "/projects/create", method = RequestMethod.GET)
	public String createProject(Model model) {
		ProjectCommand projectCommand = new ProjectCommand();
		model.addAttribute("projectCommand", projectCommand);
		return "page/project/projectCreate";
	}

	@RequestMapping(value = "/projects/create", method = RequestMethod.POST)
	public String createProject(@Valid ProjectCommand projectCommand, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "page/project/projectCreate";
		}
		Project project = new Project();
		projectCommand.toProject(project);
		project.getUsers().put(userManager.requireUserId(), Permission.OWNER);
		projectRepository.save(project);
		return "redirect:/projects/detail/" + project.getId();
	}

}
