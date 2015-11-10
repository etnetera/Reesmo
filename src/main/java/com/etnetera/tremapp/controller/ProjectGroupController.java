package com.etnetera.tremapp.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.etnetera.tremapp.model.datatables.ProjectGroupDT;
import com.etnetera.tremapp.repository.mongodb.project.ProjectGroupRepository;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;

@Controller
public class ProjectGroupController implements MenuActivityController {
	
	@Autowired
	private ProjectGroupRepository projectGroupRepository;
	
	@Override
	public String getActiveMenu() {
		return "projectGroups";
	}
	
	@RequestMapping(value = "/project-groups", method = RequestMethod.GET)
	public String projects() {
		return "page/projectGroup/projectGroups";
	}

	@RequestMapping(value = "/dt/project-groups")
	public @ResponseBody DatatablesResponse<ProjectGroupDT> findAllForDataTables(HttpServletRequest request) {
		DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
		DataSet<ProjectGroupDT> projects = projectGroupRepository.findWithDatatablesCriterias(criterias);
		return DatatablesResponse.build(projects, criterias);
	}

}
