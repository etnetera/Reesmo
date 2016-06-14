package cz.etnetera.reesmo.controller.monitor;

import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;
import cz.etnetera.reesmo.controller.MenuActivityController;
import cz.etnetera.reesmo.model.datatables.monitor.MonitorDT;
import cz.etnetera.reesmo.model.mongodb.project.Project;
import cz.etnetera.reesmo.repository.mongodb.monitor.MonitorRepository;
import cz.etnetera.reesmo.repository.mongodb.project.ProjectRepository;
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
public class MonitorsController  implements MenuActivityController {

    @Autowired
    private MonitorRepository monitorRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public String getActiveMenu() {
        return "projectMonitors";
    }


    @RequestMapping(value = "/project/monitors/{projectId}", method = RequestMethod.GET)
    public String createAnyMonitorForm(@PathVariable String projectId, Model model) {
        Project project = projectRepository.findOne(projectId);
        model.addAttribute("project", project);
        return "page/monitor/monitors";
    }

    @RequestMapping(value = "/dt/monitors/project/{projectId}")
    public @ResponseBody
    DatatablesResponse<MonitorDT> findAllMonitorsForProject(@PathVariable String projectId, HttpServletRequest request, Locale locale) {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<MonitorDT> viewMonitors = monitorRepository.findMonitorsDSForProject(projectId, criterias, locale);
        return DatatablesResponse.build(viewMonitors, criterias);
    }

    @RequestMapping(value = "/dt/monitors/view/{viewId}")
    public @ResponseBody
    DatatablesResponse<MonitorDT> findAllMonitorsForView(@PathVariable String viewId, HttpServletRequest request, Locale locale) {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<MonitorDT> viewMonitors = monitorRepository.findMonitorsDSForView(viewId, criterias, locale);
        return DatatablesResponse.build(viewMonitors, criterias);
    }
}
