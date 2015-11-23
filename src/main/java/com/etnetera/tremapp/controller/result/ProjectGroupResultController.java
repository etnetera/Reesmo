package com.etnetera.tremapp.controller.result;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.etnetera.tremapp.controller.MenuActivityController;
import com.etnetera.tremapp.http.ControllerModel;
import com.etnetera.tremapp.model.datatables.result.ResultDT;
import com.etnetera.tremapp.model.mongodb.project.ProjectGroup;
import com.etnetera.tremapp.model.mongodb.user.Permission;
import com.etnetera.tremapp.repository.elasticsearch.result.ResultRepository;
import com.etnetera.tremapp.repository.mongodb.project.ProjectGroupRepository;
import com.etnetera.tremapp.user.UserManager;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;

@Controller
public class ProjectGroupResultController implements MenuActivityController {
	
	@Autowired
    private UserManager userManager;
	
	@Autowired
	private ProjectGroupRepository projectGroupRepository;
	
	@Autowired
	private ResultRepository resultRepository;
	
	@Override
	public String getActiveMenu() {
		return "projectResults";
	}
	
	@RequestMapping(value = "/project-group/results/{projectGroupId}", method = RequestMethod.GET)
	public String results(@PathVariable String projectGroupId, Model model) {
		ProjectGroup projectGroup = projectGroupRepository.findOne(projectGroupId);
		ControllerModel.exists(projectGroup, ProjectGroup.class);
		projectGroup.checkUserPermission(userManager.requireUser(), Permission.BASIC);
		model.addAttribute("projectGroup", projectGroup);
		return "page/projectGroup/projectGroupResults";
	}

	@RequestMapping(value = "/dt/project-group/results/{projectGroupId}")
	public @ResponseBody DatatablesResponse<ResultDT> findAllForDataTables(@PathVariable String projectGroupId, HttpServletRequest request, Locale locale) {
		ProjectGroup projectGroup = projectGroupRepository.findOne(projectGroupId);
		ControllerModel.exists(projectGroup, ProjectGroup.class);
		projectGroup.checkUserPermission(userManager.requireUser(), Permission.BASIC);
		DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
		DataSet<ResultDT> results = resultRepository.findWithDatatablesCriterias(criterias, projectGroup.getProjects(), locale);
		return DatatablesResponse.build(results, criterias);
	}

}