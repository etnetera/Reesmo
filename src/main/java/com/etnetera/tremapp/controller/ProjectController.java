package com.etnetera.tremapp.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.etnetera.tremapp.model.datatables.ProjectDT;
import com.etnetera.tremapp.repository.mongodb.project.ProjectRepository;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;

@Controller
public class ProjectController implements MenuActivityController {
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Override
	public String getActiveMenu() {
		return "projects";
	}
	
	@RequestMapping(value = "/projects", method = RequestMethod.GET)
	public String projects() {
		return "page/project/projects";
	}

	@RequestMapping(value = "/dt/projects")
	public @ResponseBody DatatablesResponse<ProjectDT> findAllForDataTables(HttpServletRequest request) {
		DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
		DataSet<ProjectDT> projects = projectRepository.findWithDatatablesCriterias(criterias);
		return DatatablesResponse.build(projects, criterias);
	}

}
