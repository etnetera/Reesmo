package com.etnetera.tremapp.controller.project;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.etnetera.tremapp.controller.json.JsonResponse;
import com.etnetera.tremapp.http.ControllerModel;
import com.etnetera.tremapp.model.datatables.project.ProjectGroupProjectDT;
import com.etnetera.tremapp.model.form.project.ProjectGroupProjectAddCommand;
import com.etnetera.tremapp.model.form.project.ProjectGroupProjectRemoveCommand;
import com.etnetera.tremapp.model.mongodb.project.Project;
import com.etnetera.tremapp.model.mongodb.project.ProjectGroup;
import com.etnetera.tremapp.model.mongodb.user.Permission;
import com.etnetera.tremapp.repository.mongodb.project.ProjectGroupRepository;
import com.etnetera.tremapp.repository.mongodb.project.ProjectRepository;
import com.etnetera.tremapp.repository.mongodb.user.UserRepository;
import com.etnetera.tremapp.user.UserManager;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;

@Controller
public class ProjectGroupProjectController {

	@Autowired
    private UserManager userManager;
	
	@Autowired
	private ProjectGroupRepository projectGroupRepository;

	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private UserRepository userRepository;

	@RequestMapping(value = "/dt/project-group/projects/{projectGroupId}")
	public @ResponseBody DatatablesResponse<ProjectGroupProjectDT> findAllForDataTables(@PathVariable String projectGroupId,
			HttpServletRequest request) {
		ProjectGroup projectGroup = projectGroupRepository.findOne(projectGroupId);
		ControllerModel.exists(projectGroup, ProjectGroup.class);
		projectGroup.checkUserPermission(userManager.requireUser(), Permission.BASIC);
		DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
		DataSet<ProjectGroupProjectDT> projects = projectRepository.findProjectGroupProjectsWithDatatablesCriterias(criterias, projectGroup);
		return DatatablesResponse.build(projects, criterias);
	}

	@RequestMapping(value = "/project-group/project/add/{projectGroupId}", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JsonResponse addProjectGroupProjects(@Valid ProjectGroupProjectAddCommand addCommand,
			BindingResult bindingResult, @PathVariable String projectGroupId) {
		ProjectGroup projectGroup = projectGroupRepository.findOne(projectGroupId);
		ControllerModel.exists(projectGroup, ProjectGroup.class);
		projectGroup.checkUserPermission(userManager.requireUser(), Permission.ADMIN);
		if (bindingResult.hasErrors()) {
			return new JsonResponse(false, bindingResult.getAllErrors());
		}
		int i = 0;
		for (String projectId : addCommand.getProjectIds()) {
			Project project = projectRepository.findOne(projectId);
			if (project == null) continue;
			if (!project.isAllowedForUser(userManager.requireUser(), Permission.ADMIN)) continue;
			projectGroup.getProjects().add(project.getId());
			projectGroupRepository.save(projectGroup);
			i++;
		}
		userRepository.findAll(projectGroup.getUsers().keySet()).forEach(u -> {
			userManager.updateUserProjectsPermissions(u);
			userRepository.save(u);
			if (userManager.isSameAsLogged(u)) {
				userManager.updateUser(u);
			}
		});
		return new JsonResponse(JsonResponse.Status.SUCCESS, i);
	}
	
	@RequestMapping(value = "/project-group/project/remove/{projectGroupId}", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JsonResponse removeProjectGroupProjects(@Valid ProjectGroupProjectRemoveCommand removeCommand,
			BindingResult bindingResult, @PathVariable String projectGroupId) {
		ProjectGroup projectGroup = projectGroupRepository.findOne(projectGroupId);
		ControllerModel.exists(projectGroup, ProjectGroup.class);
		projectGroup.checkUserPermission(userManager.requireUser(), Permission.ADMIN);
		if (bindingResult.hasErrors()) {
			return new JsonResponse(false, bindingResult.getAllErrors());
		}
		int i = 0;
		for (String projectId : removeCommand.getProjectIds()) {
			Project project = projectRepository.findOne(projectId);
			if (project == null) continue;
			if (!project.isAllowedForUser(userManager.requireUser(), Permission.ADMIN)) continue;
			projectGroup.getProjects().remove(project.getId());
			projectGroupRepository.save(projectGroup);
			i++;
		}
		userRepository.findAll(projectGroup.getUsers().keySet()).forEach(u -> {
			userManager.updateUserProjectsPermissions(u);
			userRepository.save(u);
			if (userManager.isSameAsLogged(u)) {
				userManager.updateUser(u);
			}
		});
		return new JsonResponse(JsonResponse.Status.SUCCESS, i);
	}

}
