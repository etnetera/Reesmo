package com.etnetera.tremapp.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.etnetera.tremapp.http.ControllerModel;
import com.etnetera.tremapp.model.datatables.project.ProjectMemberDT;
import com.etnetera.tremapp.model.datatables.project.ProjectMemberFromGroupsDT;
import com.etnetera.tremapp.model.mongodb.project.Project;
import com.etnetera.tremapp.model.mongodb.user.Permission;
import com.etnetera.tremapp.repository.mongodb.project.ProjectRepository;
import com.etnetera.tremapp.repository.mongodb.user.UserRepository;
import com.etnetera.tremapp.user.UserHelper;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;

@Controller
public class ProjectMemberController implements MenuActivityController {

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
		UserHelper.checkProjectPermission(project.getId(), Permission.BASIC);
		DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
		DataSet<ProjectMemberDT> projects = userRepository.findProjectMembersWithDatatablesCriterias(criterias, project,
				locale);
		return DatatablesResponse.build(projects, criterias);
	}
	
	@RequestMapping(value = "/dt/projects/members-from-groups/{projectId}")
	public @ResponseBody DatatablesResponse<ProjectMemberFromGroupsDT> findAllFromGroupsForDataTables(@PathVariable String projectId,
			HttpServletRequest request, Locale locale) {
		Project project = projectRepository.findOne(projectId);
		ControllerModel.exists(project, Project.class);
		UserHelper.checkProjectPermission(project.getId(), Permission.BASIC);
		DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
		DataSet<ProjectMemberFromGroupsDT> projects = userRepository.findProjectMembersFromGroupsWithDatatablesCriterias(criterias, project,
				locale);
		return DatatablesResponse.build(projects, criterias);
	}

}
