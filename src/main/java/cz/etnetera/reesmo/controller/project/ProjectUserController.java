package cz.etnetera.reesmo.controller.project;

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

import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;

import cz.etnetera.reesmo.controller.json.JsonResponse;
import cz.etnetera.reesmo.http.ControllerModel;
import cz.etnetera.reesmo.http.exception.ForbiddenException;
import cz.etnetera.reesmo.model.datatables.project.ProjectUserDT;
import cz.etnetera.reesmo.model.datatables.project.ProjectUserFromGroupsDT;
import cz.etnetera.reesmo.model.form.project.ProjectUserAddCommand;
import cz.etnetera.reesmo.model.form.project.ProjectUserRemoveCommand;
import cz.etnetera.reesmo.model.mongodb.project.Project;
import cz.etnetera.reesmo.model.mongodb.user.Permission;
import cz.etnetera.reesmo.model.mongodb.user.User;
import cz.etnetera.reesmo.repository.mongodb.project.ProjectRepository;
import cz.etnetera.reesmo.repository.mongodb.user.UserRepository;
import cz.etnetera.reesmo.user.UserManager;

@Controller
public class ProjectUserController {

	@Autowired
	private UserManager userManager;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private UserRepository userRepository;

	@RequestMapping(value = "/dt/project/users/{projectId}")
	public @ResponseBody DatatablesResponse<ProjectUserDT> findAllForDataTables(@PathVariable String projectId,
			HttpServletRequest request, Locale locale) {
		Project project = projectRepository.findOne(projectId);
		ControllerModel.exists(project, Project.class);
		project.checkUserPermission(userManager.requireUser(), Permission.BASIC);
		DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
		DataSet<ProjectUserDT> users = userRepository.findProjectUsersWithDatatablesCriterias(criterias, project,
				locale);
		return DatatablesResponse.build(users, criterias);
	}

	@RequestMapping(value = "/dt/project/users-from-groups/{projectId}")
	public @ResponseBody DatatablesResponse<ProjectUserFromGroupsDT> findAllFromGroupsForDataTables(
			@PathVariable String projectId, HttpServletRequest request, Locale locale) {
		Project project = projectRepository.findOne(projectId);
		ControllerModel.exists(project, Project.class);
		project.checkUserPermission(userManager.requireUser(), Permission.BASIC);
		DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
		DataSet<ProjectUserFromGroupsDT> projects = userRepository
				.findProjectUsersFromGroupsWithDatatablesCriterias(criterias, project, locale);
		return DatatablesResponse.build(projects, criterias);
	}

	@RequestMapping(value = "/project/user/add/{projectId}", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JsonResponse addProjectUsers(@Valid ProjectUserAddCommand addCommand,
			BindingResult bindingResult, @PathVariable String projectId) {
		Project project = projectRepository.findOne(projectId);
		ControllerModel.exists(project, Project.class);
		project.checkUserPermission(userManager.requireUser(), Permission.ADMIN);
		if (bindingResult.hasErrors()) {
			return new JsonResponse(false, bindingResult.getAllErrors());
		}
		Permission permission = Permission.fromString(addCommand.getPermission());
		if (permission == null) {
			throw new IllegalArgumentException("Unknown permission " + addCommand.getPermission());
		}
		Permission actualUserPermission = userManager.isSuperadmin() ? Permission.OWNER
				: project.getUsers().get(userManager.requireUserId());
		if (permission.isGreaterThan(actualUserPermission)) {
			throw new ForbiddenException("User with id " + userManager.requireUserId() + " has " + actualUserPermission
					+ " permission for project with id " + projectId
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
			Permission userPermission = project.getUsers().get(userId);
			if (userPermission != null && userPermission.isGreaterThan(actualUserPermission)) {
				// do not ovewrite users with higher permission
				continue;
			}
			project.getUsers().put(user.getId(), permission);
			projectRepository.save(project);
			userManager.updateUserProjectsPermissions(user);
			userRepository.save(user);
			if (userManager.isSameAsLogged(user)) {
				userManager.updateUser(user);
			}
			i++;
		}
		return new JsonResponse(JsonResponse.Status.SUCCESS, i);
	}

	@RequestMapping(value = "/project/user/remove/{projectId}", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody JsonResponse removeProjectUsers(@Valid ProjectUserRemoveCommand removeCommand,
			BindingResult bindingResult, @PathVariable String projectId) {
		Project project = projectRepository.findOne(projectId);
		ControllerModel.exists(project, Project.class);
		project.checkUserPermission(userManager.requireUser(), Permission.ADMIN);
		if (bindingResult.hasErrors()) {
			return new JsonResponse(false, bindingResult.getAllErrors());
		}
		Permission actualUserPermission = userManager.isSuperadmin() ? Permission.OWNER
				: project.getUsers().get(userManager.requireUserId());
		int i = 0;
		for (String userId : removeCommand.getUserIds()) {
			User user = userRepository.findOne(userId);
			if (user == null)
				continue;
			if (userManager.isSameAsLogged(user) && !userManager.isSuperadmin()) {
				// do not delete yourself, unless you are superadmin
				continue;
			}
			Permission userPermission = project.getUsers().get(userId);
			if (userPermission == null || userPermission.isGreaterThan(actualUserPermission)) {
				// do not delete users with higher permission
				continue;
			}
			project.getUsers().remove(user.getId());
			projectRepository.save(project);
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
