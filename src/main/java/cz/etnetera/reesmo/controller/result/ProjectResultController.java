package cz.etnetera.reesmo.controller.result;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;

import cz.etnetera.reesmo.controller.MenuActivityController;
import cz.etnetera.reesmo.datatables.filter.FilteredDatatablesCriterias;
import cz.etnetera.reesmo.datatables.view.DatatablesView;
import cz.etnetera.reesmo.datatables.view.DatatablesViewsDefinition;
import cz.etnetera.reesmo.http.ControllerModel;
import cz.etnetera.reesmo.http.exception.NotFoundException;
import cz.etnetera.reesmo.http.exception.ValidationErrorException;
import cz.etnetera.reesmo.list.ListModifier;
import cz.etnetera.reesmo.list.filter.ListFilter;
import cz.etnetera.reesmo.message.Localizer;
import cz.etnetera.reesmo.model.datatables.result.ResultDT;
import cz.etnetera.reesmo.model.form.view.ViewCommand;
import cz.etnetera.reesmo.model.form.view.ViewCommandValidator;
import cz.etnetera.reesmo.model.mongodb.project.Project;
import cz.etnetera.reesmo.model.mongodb.user.Permission;
import cz.etnetera.reesmo.model.mongodb.view.View;
import cz.etnetera.reesmo.repository.elasticsearch.result.ResultRepository;
import cz.etnetera.reesmo.repository.mongodb.project.ProjectRepository;
import cz.etnetera.reesmo.repository.mongodb.view.ViewRepository;
import cz.etnetera.reesmo.user.UserManager;

@Controller
public class ProjectResultController implements MenuActivityController, ResultFilteredController {
	
	@Autowired
    private UserManager userManager;
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private ResultRepository resultRepository;
	
	@Autowired
	private ViewRepository viewRepository;
	
	@Autowired
	private Localizer localizer;
	
	@Override
	public String getActiveMenu() {
		return "projectResults";
	}
	
	@InitBinder(value = "viewCommand")
	protected void initBinder(WebDataBinder binder) {		
		binder.addValidators(new ViewCommandValidator());
	}
	
	@RequestMapping(value = "/project/results/{projectId}", method = RequestMethod.GET)
	public String results(@PathVariable String projectId, Model model, Locale locale) {
		Project project = projectRepository.findOne(projectId);
		ControllerModel.exists(project, Project.class);
		project.checkUserPermission(userManager.requireUser(), Permission.BASIC);
		model.addAttribute("project", project);
		injectFiltersDefinition(model, localizer, locale);
		injectViewsDefinition(model, project, null);
		if (project.isAllowedForUser(userManager.requireUser(), Permission.ADMIN)) {
			ViewCommand viewCommand = new ViewCommand();
			model.addAttribute("viewCommand", viewCommand);
		}
		return "page/project/projectResults";
	}
	
	@RequestMapping(value = "/project/results/{projectId}/view/{viewId}", method = RequestMethod.GET)
	public String results(@PathVariable String projectId, @PathVariable String viewId, Model model, Locale locale) {
		Project project = projectRepository.findOne(projectId);
		ControllerModel.exists(project, Project.class);
		project.checkUserPermission(userManager.requireUser(), Permission.BASIC);

		View view = viewRepository.findOne(viewId);
		ControllerModel.exists(view, View.class);

		if (!view.getProjectId().equals(project.getId()))
			throw new NotFoundException("View " + view.getId() + " not found under project " + project.getId() + ".");

		model.addAttribute("project", project);
		injectFiltersDefinition(model, localizer, locale);
		injectFiltersState(model, view.getModifier().getFilters());
		injectViewsDefinition(model, project, view);
		if (project.isAllowedForUser(userManager.requireUser(), Permission.ADMIN)) {
			ViewCommand viewCommand = new ViewCommand();
			model.addAttribute("viewCommand", viewCommand);
		}
		return "page/project/projectResults";
	}

	@RequestMapping(value = "/project/results/{projectId}/view/create", method = RequestMethod.POST)
	public String createViewFromFilters(@Valid ViewCommand viewCommand,
			BindingResult bindingResult, @PathVariable String projectId, HttpServletRequest request, Model model, Locale locale) throws Exception {
		Project project = projectRepository.findOne(projectId);
		ControllerModel.exists(project, Project.class);
		project.checkUserPermission(userManager.requireUser(), Permission.ADMIN);

		List<ListFilter> filters = FilteredDatatablesCriterias.getFiltersFromRequest(request);
		if (filters == null) {
			throw new ValidationErrorException("Filters are empty or not well-formed.");
		}

		if (bindingResult.hasErrors()) {
			model.addAttribute("project", project);
			injectFiltersDefinition(model, localizer, locale);
			injectFiltersState(model, filters);
			injectViewsDefinition(model, project, null);
			return "page/project/projectResults";
		}

		View view = new View();
		viewCommand.toView(view);
		view.setProjectId(project.getId());

		ListModifier modifier = new ListModifier();
		modifier.setFilters(filters);
		view.setModifier(modifier);
		view.setProjectId(projectId);
		viewRepository.save(view);
		
		return "redirect:/project/results/" + projectId + "/view/" + view.getId();
	}

	@RequestMapping(value = "/dt/project/results/{projectId}")
	public @ResponseBody DatatablesResponse<ResultDT> findAllForDataTables(@PathVariable String projectId, HttpServletRequest request, Locale locale) throws Exception {
		Project project = projectRepository.findOne(projectId);
		ControllerModel.exists(project, Project.class);
		project.checkUserPermission(userManager.requireUser(), Permission.BASIC);
		FilteredDatatablesCriterias criterias = FilteredDatatablesCriterias.getFromRequest(request);
		DataSet<ResultDT> results = resultRepository.findWithFilteredDatatablesCriterias(criterias, Arrays.asList(projectId), locale);
		return DatatablesResponse.build(results, criterias.getCriterias());
	}

	private void injectViewsDefinition(Model model, Project project, View activeView) {
		DatatablesViewsDefinition datatablesViewsDef = new DatatablesViewsDefinition();
		viewRepository.findByProject(project.getId()).forEach(v -> datatablesViewsDef.addView(DatatablesView.fromView(v)));
		if (activeView != null)
			datatablesViewsDef.setActiveViewId(activeView.getId());
		model.addAttribute("datatablesViewsDef", datatablesViewsDef);
	}

}
