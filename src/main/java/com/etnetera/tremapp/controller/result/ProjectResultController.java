package com.etnetera.tremapp.controller.result;

import java.util.Arrays;
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
import com.etnetera.tremapp.model.mongodb.project.Project;
import com.etnetera.tremapp.model.mongodb.user.Permission;
import com.etnetera.tremapp.repository.elasticsearch.result.ResultRepository;
import com.etnetera.tremapp.repository.mongodb.project.ProjectRepository;
import com.etnetera.tremapp.user.UserManager;
import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;

@Controller
public class ProjectResultController implements MenuActivityController {
	
	@Autowired
    private UserManager userManager;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private ResultRepository resultRepository;
	
	@Override
	public String getActiveMenu() {
		return "projectResults";
	}
	
	@RequestMapping(value = "/project/results/{projectId}", method = RequestMethod.GET)
	public String results(@PathVariable String projectId, Model model) {
		Project project = projectRepository.findOne(projectId);
		ControllerModel.exists(project, Project.class);
		project.checkUserPermission(userManager.requireUser(), Permission.BASIC);
		model.addAttribute("project", project);
		return "page/project/projectResults";
	}

	@RequestMapping(value = "/dt/project/results/{projectId}")
	public @ResponseBody DatatablesResponse<ResultDT> findAllForDataTables(@PathVariable String projectId, HttpServletRequest request, Locale locale) {
		Project project = projectRepository.findOne(projectId);
		ControllerModel.exists(project, Project.class);
		project.checkUserPermission(userManager.requireUser(), Permission.BASIC);
		DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
		DataSet<ResultDT> results = resultRepository.findWithDatatablesCriterias(criterias, Arrays.asList(projectId), locale);
		return DatatablesResponse.build(results, criterias);
	}

}
