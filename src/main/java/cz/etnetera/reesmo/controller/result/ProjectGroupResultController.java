package cz.etnetera.reesmo.controller.result;

import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;
import cz.etnetera.reesmo.controller.MenuActivityController;
import cz.etnetera.reesmo.datatables.filter.FilteredDatatablesCriterias;
import cz.etnetera.reesmo.http.ControllerModel;
import cz.etnetera.reesmo.message.Localizer;
import cz.etnetera.reesmo.model.datatables.result.ResultDT;
import cz.etnetera.reesmo.model.mongodb.project.ProjectGroup;
import cz.etnetera.reesmo.model.mongodb.user.Permission;
import cz.etnetera.reesmo.repository.elasticsearch.result.ResultRepository;
import cz.etnetera.reesmo.repository.mongodb.project.ProjectGroupRepository;
import cz.etnetera.reesmo.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Controller
public class ProjectGroupResultController implements MenuActivityController, ResultFilteredController {
	
	@Autowired
    private UserManager userManager;
	
	@Autowired
	private ProjectGroupRepository projectGroupRepository;
	
	@Autowired
	private ResultRepository resultRepository;
	
	@Autowired
	private Localizer localizer;
	
	@Override
	public String getActiveMenu() {
		return "projectGroupResults";
	}
	
	@RequestMapping(value = "/project-group/results/{projectGroupId}", method = RequestMethod.GET)
	public String results(@PathVariable String projectGroupId, Model model, Locale locale) {
		ProjectGroup projectGroup = projectGroupRepository.findOne(projectGroupId);
		ControllerModel.exists(projectGroup, ProjectGroup.class);
		projectGroup.checkUserPermission(userManager.requireUser(), Permission.BASIC);
		model.addAttribute("projectGroup", projectGroup);
		injectFiltersDefinition(model, localizer, locale);
		return "page/projectGroup/projectGroupResults";
	}

	@RequestMapping(value = "/dt/project-group/results/{projectGroupId}")
	public @ResponseBody DatatablesResponse<ResultDT> findAllForDataTables(@PathVariable String projectGroupId, HttpServletRequest request, Locale locale) throws Exception {
		ProjectGroup projectGroup = projectGroupRepository.findOne(projectGroupId);
		ControllerModel.exists(projectGroup, ProjectGroup.class);
		projectGroup.checkUserPermission(userManager.requireUser(), Permission.BASIC);
		FilteredDatatablesCriterias criterias = FilteredDatatablesCriterias.getFromRequest(request);
		DataSet<ResultDT> results = resultRepository.findWithFilteredDatatablesCriterias(criterias, projectGroup.getProjects(), locale);
		return DatatablesResponse.build(results, criterias.getCriterias());
	}

}
