package com.etnetera.tremapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.etnetera.tremapp.model.datatables.project.ProjectGroupDT;
import com.etnetera.tremapp.model.form.project.ProjectGroupCommand;
import com.etnetera.tremapp.model.mongodb.project.ProjectGroup;
import com.etnetera.tremapp.model.mongodb.user.Permission;
import com.etnetera.tremapp.repository.mongodb.project.ProjectGroupRepository;
import com.etnetera.tremapp.user.UserManager;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;

@Controller
public class ProjectGroupsController implements MenuActivityController {
	
	@Autowired
    private UserManager userManager;
	
	@Autowired
	private ProjectGroupRepository projectGroupRepository;
	
	@Override
	public String getActiveMenu() {
		return "projectGroups";
	}
	
	@RequestMapping(value = "/project-groups", method = RequestMethod.GET)
	public String projectGroups() {
		return "page/projectGroup/projectGroups";
	}

	@RequestMapping(value = "/dt/project-groups")
	public @ResponseBody DatatablesResponse<ProjectGroupDT> findAllForDataTables(HttpServletRequest request) {
		DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
		DataSet<ProjectGroupDT> projectGroups = projectGroupRepository.findWithDatatablesCriterias(criterias, userManager.isSuperadmin() ? null : userManager.requireUserId());
		return DatatablesResponse.build(projectGroups, criterias);
	}

	@RequestMapping(value = "/project-groups/create", method = RequestMethod.GET)
	public String createProjectGroup(Model model) {
		ProjectGroupCommand projectGroupCommand = new ProjectGroupCommand();
		model.addAttribute("projectGroupCommand", projectGroupCommand);
		return "page/projectGroup/projectGroupCreate";
	}

	@RequestMapping(value = "/project-groups/create", method = RequestMethod.POST)
	public String createProjectGroup(@Valid ProjectGroupCommand projectGroupCommand, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "page/projectGroup/projectGroupCreate";
		}
		ProjectGroup projectGroup = new ProjectGroup();
		projectGroupCommand.toProject(projectGroup);
		projectGroup.getUsers().put(userManager.requireUserId(), Permission.OWNER);
		projectGroupRepository.save(projectGroup);
		return "redirect:/project-groups/detail/" + projectGroup.getId();
	}

}
