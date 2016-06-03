package cz.etnetera.reesmo.controller.view;

import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;
import cz.etnetera.reesmo.controller.MenuActivityController;
import cz.etnetera.reesmo.datatables.filter.FilteredDatatablesCriterias;
import cz.etnetera.reesmo.http.ControllerModel;
import cz.etnetera.reesmo.http.exception.ValidationErrorException;
import cz.etnetera.reesmo.list.ListModifier;
import cz.etnetera.reesmo.list.filter.ListFilter;
import cz.etnetera.reesmo.model.datatables.view.ViewDT;
import cz.etnetera.reesmo.model.form.view.ViewCommand;
import cz.etnetera.reesmo.model.form.view.ViewCommandValidator;
import cz.etnetera.reesmo.model.mongodb.project.Project;
import cz.etnetera.reesmo.model.mongodb.user.Permission;
import cz.etnetera.reesmo.model.mongodb.view.View;
import cz.etnetera.reesmo.repository.mongodb.project.ProjectRepository;
import cz.etnetera.reesmo.repository.mongodb.view.ViewRepository;
import cz.etnetera.reesmo.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
public class ProjectViewsController implements MenuActivityController {

    @Autowired
    private UserManager userManager;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ViewRepository viewRepository;

    @Override
    public String getActiveMenu() {
        return "projectViews";
    }

    @InitBinder(value = "viewCommand")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(new ViewCommandValidator());
    }

    @RequestMapping(value = "/view/create/{projectId}", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    View createProjectView(@Valid ViewCommand viewCommand,
                           BindingResult bindingResult, @PathVariable String projectId, HttpServletRequest request, Locale locale) throws Exception {
        Project project = projectRepository.findOne(projectId);
        ControllerModel.exists(project, Project.class);
        project.checkUserPermission(userManager.requireUser(), Permission.EDITOR);

        if (bindingResult.hasErrors()) {
            throw new ValidationErrorException(bindingResult.getAllErrors().stream().map(er -> er.toString()).collect(Collectors
                    .joining(". ")));
        }
        View view = new View();
        viewCommand.toView(view);

        List<ListFilter> filters = FilteredDatatablesCriterias.getFiltersFromRequest(request);
        if (filters == null) {
            throw new ValidationErrorException("Filters are empty or not well-formed.");
        }
        ListModifier modifier = new ListModifier();
        modifier.setFilters(filters);
        view.setModifier(modifier);
        viewRepository.save(view);
        return view;
    }

    @RequestMapping(value = "/views/{projectId}", method = RequestMethod.GET)
    public String views(@PathVariable String projectId, Model model) {
        Project project = projectRepository.findOne(projectId);
        ControllerModel.exists(project, Project.class);
        project.checkUserPermission(userManager.requireUser(), Permission.BASIC);
        model.addAttribute("project", project);
        return "page/view/views";
    }

    @RequestMapping(value = "/dt/views/{projectId}")
    public @ResponseBody
    DatatablesResponse<ViewDT> findAllForDataTables(@PathVariable String projectId, HttpServletRequest request) {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<ViewDT> views = viewRepository.findViewsForProjectWithCriterias(criterias, projectId);
        return DatatablesResponse.build(views, criterias);
    }
}
