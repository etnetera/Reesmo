package com.etnetera.tremapp.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.etnetera.tremapp.controller.json.JsonResponse;
import com.etnetera.tremapp.http.ControllerModel;
import com.etnetera.tremapp.model.datatables.project.ProjectMemberDT;
import com.etnetera.tremapp.model.datatables.project.ProjectMemberFromGroupsDT;
import com.etnetera.tremapp.model.form.project.ProjectMemberAddCommand;
import com.etnetera.tremapp.model.form.project.ProjectMemberRemoveCommand;
import com.etnetera.tremapp.model.mongodb.project.Project;
import com.etnetera.tremapp.model.mongodb.user.Permission;
import com.etnetera.tremapp.model.mongodb.user.User;
import com.etnetera.tremapp.repository.mongodb.project.ProjectRepository;
import com.etnetera.tremapp.repository.mongodb.user.UserRepository;
import com.etnetera.tremapp.user.UserManager;
import com.etnetera.tremapp.user.UserRole;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;

@Controller
public class ProjectMemberController implements MenuActivityController {

	@Autowired
    private UserManager userManager;
	
	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	public String getActiveMenu() {
		return "projects";
	}

	@RequestMapping(value = "/dt/projects/members/{projectId}")
	public @ResponseBody DatatablesResponse<ProjectMemberDT> findAllForDataTables(@PathVariable String projectId,
			HttpServletRequest request, Locale locale) {
		Project project = projectRepository.findOne(projectId);
		ControllerModel.exists(project, Project.class);
		userManager.checkProjectPermission(project.getId(), Permission.BASIC);
		DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
		DataSet<ProjectMemberDT> projects = userRepository.findProjectMembersWithDatatablesCriterias(criterias, project,
				locale);
		return DatatablesResponse.build(projects, criterias);
	}

	@RequestMapping(value = "/dt/projects/members-from-groups/{projectId}")
	public @ResponseBody DatatablesResponse<ProjectMemberFromGroupsDT> findAllFromGroupsForDataTables(
			@PathVariable String projectId, HttpServletRequest request, Locale locale) {
		Project project = projectRepository.findOne(projectId);
		ControllerModel.exists(project, Project.class);
		userManager.checkProjectPermission(project.getId(), Permission.BASIC);
		DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
		DataSet<ProjectMemberFromGroupsDT> projects = userRepository
				.findProjectMembersFromGroupsWithDatatablesCriterias(criterias, project, locale);
		return DatatablesResponse.build(projects, criterias);
	}

	@Secured({ UserRole.ROLE_ADMIN })
	@RequestMapping(value = "/projects/member/add/{projectId}", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JsonResponse addProjectMembers(@Valid ProjectMemberAddCommand addCommand,
			BindingResult bindingResult, @PathVariable String projectId) {
		Project project = projectRepository.findOne(projectId);
		ControllerModel.exists(project, Project.class);
		if (bindingResult.hasErrors()) {
			return new JsonResponse(false, bindingResult.getAllErrors());
		}
		Permission permission = Permission.fromString(addCommand.getPermission());
		if (permission == null) {
			throw new IllegalArgumentException("Uknown permission " + addCommand.getPermission());
		}
		int i = 0;
		for (String userId : addCommand.getUserIds()) {
			User user = userRepository.findOne(userId);
			if (user == null) continue;
			project.getMembers().put(user.getId(), permission);
			projectRepository.save(project);
			userManager.updateUserProjectsPermissions(user);
			userRepository.save(user);
			i++;
		}
		return new JsonResponse(JsonResponse.Status.SUCCESS, i);
	}
	
	@Secured({ UserRole.ROLE_ADMIN })
	@RequestMapping(value = "/projects/member/remove/{projectId}", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JsonResponse removeProjectMembers(@Valid ProjectMemberRemoveCommand removeCommand,
			BindingResult bindingResult, @PathVariable String projectId) {
		Project project = projectRepository.findOne(projectId);
		ControllerModel.exists(project, Project.class);
		if (bindingResult.hasErrors()) {
			return new JsonResponse(false, bindingResult.getAllErrors());
		}
		int i = 0;
		for (String userId : removeCommand.getUserIds()) {
			User user = userRepository.findOne(userId);
			if (user == null) continue;
			project.getMembers().remove(user.getId());
			projectRepository.save(project);
			userManager.updateUserProjectsPermissions(user);
			userRepository.save(user);
			i++;
		}
		return new JsonResponse(JsonResponse.Status.SUCCESS, i);
	}

}
