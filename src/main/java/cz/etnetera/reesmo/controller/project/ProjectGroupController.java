package cz.etnetera.reesmo.controller.project;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cz.etnetera.reesmo.controller.MenuActivityController;
import cz.etnetera.reesmo.http.ControllerModel;
import cz.etnetera.reesmo.model.form.project.ProjectGroupCommand;
import cz.etnetera.reesmo.model.mongodb.project.ProjectGroup;
import cz.etnetera.reesmo.model.mongodb.user.Permission;
import cz.etnetera.reesmo.repository.mongodb.project.ProjectGroupRepository;
import cz.etnetera.reesmo.user.UserManager;

@Controller
public class ProjectGroupController implements MenuActivityController {
	
	@Autowired
    private UserManager userManager;
	
	@Autowired
	private ProjectGroupRepository projectGroupRepository;
	
	@Override
	public String getActiveMenu() {
		return "projectGroupSettings";
	}
	
	@RequestMapping(value = "/project-group/home/{projectGroupId}", method = RequestMethod.GET)
	public String projectGroupHome(@PathVariable String projectGroupId) {
		return "redirect:/project-group/results/" + projectGroupId;
	}
	
	@RequestMapping(value = "/project-group/detail/{projectGroupId}", method = RequestMethod.GET)
	public String showProjectGroup(@PathVariable String projectGroupId, Model model) {
		ProjectGroup projectGroup = projectGroupRepository.findOne(projectGroupId);
		ControllerModel.exists(projectGroup, ProjectGroup.class);
		projectGroup.checkUserPermission(userManager.requireUser(), Permission.BASIC);
		model.addAttribute("projectGroup", projectGroup);
		return "page/projectGroup/projectGroupDetail";
	}

	@RequestMapping(value = "/project-group/edit/{projectGroupId}", method = RequestMethod.GET)
	public String editProjectGroup(@PathVariable String projectGroupId, Model model) {
		ProjectGroup projectGroup = projectGroupRepository.findOne(projectGroupId);
		ControllerModel.exists(projectGroup, ProjectGroup.class);
		projectGroup.checkUserPermission(userManager.requireUser(), Permission.ADMIN);
		ProjectGroupCommand projectGroupCommand = new ProjectGroupCommand();
		projectGroupCommand.fromProject(projectGroup);
		model.addAttribute("projectGroup", projectGroup);
		model.addAttribute("projectGroupCommand", projectGroupCommand);
		return "page/projectGroup/projectGroupEdit";
	}

	@RequestMapping(value = "/project-group/edit/{projectGroupId}", method = RequestMethod.POST)
	public String editProjectGroup(@Valid ProjectGroupCommand projectGroupCommand,
			BindingResult bindingResult, @PathVariable String projectGroupId, Model model) {
		ProjectGroup projectGroup = projectGroupRepository.findOne(projectGroupId);
		ControllerModel.exists(projectGroup, ProjectGroup.class);
		projectGroup.checkUserPermission(userManager.requireUser(), Permission.ADMIN);
		if (bindingResult.hasErrors()) {
			model.addAttribute("projectGroup", projectGroup);
			return "page/projectGroup/projectGroupEdit";
		}
		projectGroupCommand.toProject(projectGroup);
		projectGroupRepository.save(projectGroup);
		return "redirect:/project-group/detail/" + projectGroup.getId();
	}

	@RequestMapping(value = "/project-group/delete/{projectGroupId}", method = RequestMethod.GET)
	public String deleteProjectGroup(@PathVariable String projectGroupId, Model model) {
		ProjectGroup projectGroup = projectGroupRepository.findOne(projectGroupId);
		ControllerModel.exists(projectGroup, ProjectGroup.class);
		projectGroup.checkUserPermission(userManager.requireUser(), Permission.OWNER);
		model.addAttribute("projectGroup", projectGroup);
		return "page/projectGroup/projectGroupDelete";
	}

	@RequestMapping(value = "/project-group/delete/{projectGroupId}", method = RequestMethod.POST)
	public String deleteProjectGroup(@PathVariable String projectGroupId) {
		ProjectGroup projectGroup = projectGroupRepository.findOne(projectGroupId);
		ControllerModel.exists(projectGroup, ProjectGroup.class);
		projectGroup.checkUserPermission(userManager.requireUser(), Permission.OWNER);
		projectGroupRepository.delete(projectGroup);
		return "redirect:/project-groups";
	}

}
