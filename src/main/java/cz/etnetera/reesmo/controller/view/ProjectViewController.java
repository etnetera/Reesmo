package cz.etnetera.reesmo.controller.view;

import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;
import cz.etnetera.reesmo.controller.MenuActivityController;
import cz.etnetera.reesmo.controller.result.ResultFilteredController;
import cz.etnetera.reesmo.datatables.filter.FilteredDatatablesCriterias;
import cz.etnetera.reesmo.http.ControllerModel;
import cz.etnetera.reesmo.message.Localizer;
import cz.etnetera.reesmo.model.datatables.result.ResultDT;
import cz.etnetera.reesmo.model.form.view.ViewCommandValidator;
import cz.etnetera.reesmo.model.mongodb.project.Project;
import cz.etnetera.reesmo.model.mongodb.user.Permission;
import cz.etnetera.reesmo.model.mongodb.view.View;
import cz.etnetera.reesmo.repository.elasticsearch.result.ResultRepository;
import cz.etnetera.reesmo.repository.mongodb.project.ProjectRepository;
import cz.etnetera.reesmo.repository.mongodb.view.ViewRepository;
import cz.etnetera.reesmo.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Locale;

@Controller
public class ProjectViewController implements MenuActivityController, ResultFilteredController {
	
	@Autowired
    private UserManager userManager;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private ViewRepository viewRepository;

	@Autowired
	private ResultRepository resultRepository;

	@Autowired
	private Localizer localizer;
	
	@Override
	public String getActiveMenu() {
		return "projectViews";
	}
	
	@InitBinder(value = "viewCommand")
	protected void initBinder(WebDataBinder binder) {		
		binder.addValidators(new ViewCommandValidator());
	}


	@RequestMapping(value = "/view/{viewId}", method = RequestMethod.GET)
	public String view(@PathVariable String viewId, Model model) {
		View view = viewRepository.findOne(viewId);
		Project project = projectRepository.findOne(view.getProjectId());
		ControllerModel.exists(project, Project.class);
		project.checkUserPermission(userManager.requireUser(), Permission.BASIC);
		model.addAttribute("view", view);
		model.addAttribute("project", project);
		return "page/view/view";
	}

	@RequestMapping(value = "/dt/results/view/{viewId}")
	public @ResponseBody
	DatatablesResponse<ResultDT> findAllviewResultsForDataTables(@PathVariable String viewId, HttpServletRequest request, Locale locale) throws Exception {
		View view = viewRepository.findOne(viewId);
		Project project = projectRepository.findOne(view.getId());
		ControllerModel.exists(project, Project.class);

		FilteredDatatablesCriterias criterias = FilteredDatatablesCriterias.getFromRequest(request);
		DataSet<ResultDT> results = resultRepository.findWithFilteredDatatablesCriterias(criterias, Arrays.asList(project.getId()), locale);
		//TODO filter results to match certain view
		return DatatablesResponse.build(results, criterias.getCriterias());
	}


	@RequestMapping(value = "/view/edit/{viewId}", method = RequestMethod.GET)
	public String editView(@PathVariable String viewId, Model model) {
		model.addAttribute("view", viewRepository.findOne(viewId));
		return "page/view/viewEdit";
	}

	@RequestMapping(value = "/view/edit/{viewId}", method = RequestMethod.POST)
	public String editView(@ModelAttribute View view) {
		// TODO
		return "page/view/viewEdit";
	}



	@RequestMapping(value = "/view/delete/{viewId}", method = RequestMethod.GET)
	public String deleteView(@PathVariable String viewId, Model model) {
		model.addAttribute("view", viewRepository.findOne(viewId));
		return "page/view/viewDelete";
	}

	@RequestMapping(value = "/view/delete/{viewId}", method = RequestMethod.POST)
	public String deleteView(@PathVariable String viewId) {
		viewRepository.delete(viewId);
		//TODO delete monitors
		return "page/view/viewDelete";
	}



}
