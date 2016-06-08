package cz.etnetera.reesmo.controller.monitor;

import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;
import cz.etnetera.reesmo.model.datatables.monitor.MonitorDT;
import cz.etnetera.reesmo.model.form.monitor.MonitorCommand;
import cz.etnetera.reesmo.model.form.monitor.MonitorCommandValidator;
import cz.etnetera.reesmo.model.mongodb.monitoring.AnyMonitoring;
import cz.etnetera.reesmo.model.mongodb.monitoring.FlatlineMonitoring;
import cz.etnetera.reesmo.model.mongodb.monitoring.FrequencyMonitoring;
import cz.etnetera.reesmo.model.mongodb.project.Project;
import cz.etnetera.reesmo.model.mongodb.view.View;
import cz.etnetera.reesmo.repository.mongodb.monitor.MonitorRepository;
import cz.etnetera.reesmo.repository.mongodb.project.ProjectRepository;
import cz.etnetera.reesmo.repository.mongodb.view.ViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

@Controller
public class MonitorController {


    @Autowired
    MonitorRepository monitorRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ViewRepository viewRepository;

    @InitBinder(value = "monitorCommand")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(new MonitorCommandValidator());
    }


    @RequestMapping(value = "/view/{viewId}/monitor/frequency/create", method = RequestMethod.GET)
    public String createFrequencyMonitorForm(@PathVariable String viewId, Model model) {
        View view = viewRepository.findOne(viewId);
        Project project = projectRepository.findOne(view.getProjectId());
        model.addAttribute("monitorCommand", new MonitorCommand(TimeUnit.HOURS, 0, 0));
        model.addAttribute("project", project);
        model.addAttribute("view", view);
        return "page/monitor/frequencyMonitorCreate";
    }

    @RequestMapping(value = "/view/{viewId}/monitor/frequency/create", method = RequestMethod.POST)
    public String createFrequencyMonitor(Model model, @PathVariable String viewId, @Valid @ModelAttribute MonitorCommand monitorCommand, BindingResult result) {
        if (result.hasErrors()){
            View view = viewRepository.findOne(viewId);
            Project project = projectRepository.findOne(view.getProjectId());
            model.addAttribute("project", project);
            model.addAttribute("view", view);
            return "page/monitor/frequencyMonitorCreate";
        }
        FrequencyMonitoring frequencyMonitoring = new FrequencyMonitoring();
        monitorCommand.toMonitor(frequencyMonitoring);
        frequencyMonitoring.setViewId(viewId);
        frequencyMonitoring.setEnabled(true);
        monitorRepository.save(frequencyMonitoring);
        return "redirect:/view/" + viewId;
    }

    @RequestMapping(value = "/view/{viewId}/monitor/flatline/create", method = RequestMethod.GET)
    public String createFlatlineMonitorForm(@PathVariable String viewId, Model model) {
        View view = viewRepository.findOne(viewId);
        Project project = projectRepository.findOne(view.getProjectId());
        model.addAttribute("monitorCommand", new MonitorCommand(TimeUnit.HOURS, 0, 0));
        model.addAttribute("project", project);
        model.addAttribute("view", view);
        return "page/monitor/flatlineMonitorCreate";
    }

    @RequestMapping(value = "/view/{viewId}/monitor/flatline/create", method = RequestMethod.POST)
    public String createFlatlineMonitor(Model model, @PathVariable String viewId, @Valid @ModelAttribute MonitorCommand monitorCommand, BindingResult result) {
        if (result.hasErrors()){
            View view = viewRepository.findOne(viewId);
            Project project = projectRepository.findOne(view.getProjectId());
            model.addAttribute("project", project);
            model.addAttribute("view", view);
            return "page/monitor/frequencyMonitorCreate";
        }
        FlatlineMonitoring flatlineMonitoring = new FlatlineMonitoring();
        monitorCommand.toMonitor(flatlineMonitoring);
        flatlineMonitoring.setViewId(viewId);
        flatlineMonitoring.setEnabled(true);
        monitorRepository.save(flatlineMonitoring);
        return "redirect:/view/" + viewId;
    }

    @RequestMapping(value = "/view/{viewId}/monitor/any/create", method = RequestMethod.GET)
    public String createAnyMonitorForm(@PathVariable String viewId, Model model) {
        View view = viewRepository.findOne(viewId);
        Project project = projectRepository.findOne(view.getProjectId());
        model.addAttribute("project", project);
        model.addAttribute("view", view);
        return "page/monitor/anyMonitorCreate";
    }

    @RequestMapping(value = "/view/{viewId}/monitor/any/create", method = RequestMethod.POST)
    public String createAnyMonitor(Model model, @PathVariable String viewId) {
        AnyMonitoring anyMonitoring = new AnyMonitoring();
        anyMonitoring.setViewId(viewId);
        anyMonitoring.setEnabled(true);
        monitorRepository.save(anyMonitoring);
        return "redirect:/view/" + viewId;
    }


    @RequestMapping(value = "/dt/monitors/{viewId}")
    public @ResponseBody
    DatatablesResponse<MonitorDT> findAllForDataTables(@PathVariable String viewId, HttpServletRequest request) {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<MonitorDT> viewMonitors = monitorRepository.findViewMonitors(viewId);
//        DataSet<ProjectDT> projects = projectRepository.findWithDatatablesCriterias(criterias, userManager.getAllowedProjectIds(perm));
        return DatatablesResponse.build(viewMonitors, criterias);
    }

}
