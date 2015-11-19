package com.etnetera.tremapp.controller;

import java.util.Locale;

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
import com.etnetera.tremapp.http.exception.ForbiddenException;
import com.etnetera.tremapp.model.datatables.project.ProjectGroupUserDT;
import com.etnetera.tremapp.model.form.project.ProjectGroupUserAddCommand;
import com.etnetera.tremapp.model.form.project.ProjectGroupUserRemoveCommand;
import com.etnetera.tremapp.model.mongodb.project.ProjectGroup;
import com.etnetera.tremapp.model.mongodb.user.Permission;
import com.etnetera.tremapp.model.mongodb.user.User;
import com.etnetera.tremapp.repository.mongodb.project.ProjectGroupRepository;
import com.etnetera.tremapp.repository.mongodb.user.UserRepository;
import com.etnetera.tremapp.user.UserManager;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;

@Controller
public class ProjectGroupUserController {

	@Autowired
	private UserManager userManager;

	@Autowired
	private ProjectGroupRepository projectGroupRepository;

	@Autowired
	private UserRepository userRepository;

	@RequestMapping(value = "/dt/project-groups/users/{projectGroupId}")
	public @ResponseBody DatatablesResponse<ProjectGroupUserDT> findAllForDataTables(
			@PathVariable String projectGroupId, HttpServletRequest request, Locale locale) {
		ProjectGroup projectGroup = projectGroupRepository.findOne(projectGroupId);
		ControllerModel.exists(projectGroup, ProjectGroup.class);
		projectGroup.checkUserPermission(userManager.requireUser(), Permission.BASIC);
		DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
		DataSet<ProjectGroupUserDT> users = userRepository.findProjectGroupUsersWithDatatablesCriterias(criterias,
				projectGroup, locale);
		return DatatablesResponse.build(users, criterias);
	}

	@RequestMapping(value = "/project-groups/user/add/{projectGroupId}", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JsonResponse addProjectGroupUsers(@Valid ProjectGroupUserAddCommand addCommand,
			BindingResult bindingResult, @PathVariable String projectGroupId) {
		ProjectGroup projectGroup = projectGroupRepository.findOne(projectGroupId);
		ControllerModel.exists(projectGroup, ProjectGroup.class);
		projectGroup.checkUserPermission(userManager.requireUser(), Permission.ADMIN);
		if (bindingResult.hasErrors()) {
			return new JsonResponse(false, bindingResult.getAllErrors());
		}
		Permission permission = Permission.fromString(addCommand.getPermission());
		if (permission == null) {
			throw new IllegalArgumentException("Uknown permission " + addCommand.getPermission());
		}
		Permission actualUserPermission = projectGroup.getUsers().get(userManager.requireUserId());
		if (permission.isGreaterThan(actualUserPermission)) {
			throw new ForbiddenException("User with id " + userManager.requireUserId() + " has " + actualUserPermission
					+ " permission for project group with id " + projectGroupId
					+ " and therefore is not allowed to add users with " + permission + " permission.");
		}
		int i = 0;
		for (String userId : addCommand.getUserIds()) {
			User user = userRepository.findOne(userId);
			if (user == null)
				continue;
			if (userManager.isSameAsLogged(user) && !userManager.isSuperadmin()) {
				// do not add yourself, unless you are superadmin
				continue;
			}
			projectGroup.getUsers().put(user.getId(), permission);
			projectGroupRepository.save(projectGroup);
			userManager.updateUserProjectsPermissions(user);
			userRepository.save(user);
			if (userManager.isSameAsLogged(user)) {
				userManager.updateUser(user);
			}
			i++;
		}
		return new JsonResponse(JsonResponse.Status.SUCCESS, i);
	}

	@RequestMapping(value = "/project-groups/user/remove/{projectGroupId}", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JsonResponse removeProjectGroupUsers(@Valid ProjectGroupUserRemoveCommand removeCommand,
			BindingResult bindingResult, @PathVariable String projectGroupId) {
		ProjectGroup projectGroup = projectGroupRepository.findOne(projectGroupId);
		ControllerModel.exists(projectGroup, ProjectGroup.class);
		projectGroup.checkUserPermission(userManager.requireUser(), Permission.ADMIN);
		if (bindingResult.hasErrors()) {
			return new JsonResponse(false, bindingResult.getAllErrors());
		}
		Permission actualUserPermission = projectGroup.getUsers().get(userManager.requireUserId());
		int i = 0;
		for (String userId : removeCommand.getUserIds()) {
			User user = userRepository.findOne(userId);
			if (user == null)
				continue;
			if (userManager.isSameAsLogged(user) && !userManager.isSuperadmin()) {
				// do not delete yourself, unless you are superadmin
				continue;
			}
			Permission userPermission = projectGroup.getUsers().get(userId);
			if (userPermission == null || userPermission.isGreaterThan(actualUserPermission)) {
				// do not delete users with higher permission
				continue;
			}
			projectGroup.getUsers().remove(user.getId());
			projectGroupRepository.save(projectGroup);
			userManager.updateUserProjectsPermissions(user);
			userRepository.save(user);
			if (userManager.isSameAsLogged(user)) {
				userManager.updateUser(user);
			}
			i++;
		}
		return new JsonResponse(JsonResponse.Status.SUCCESS, i);
	}

}
