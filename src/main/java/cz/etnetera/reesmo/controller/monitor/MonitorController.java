package cz.etnetera.reesmo.controller.monitor;

import cz.etnetera.reesmo.controller.MenuActivityController;
import cz.etnetera.reesmo.http.ControllerModel;
import cz.etnetera.reesmo.message.Localizer;
import cz.etnetera.reesmo.model.datatables.monitor.MonitorDT;
import cz.etnetera.reesmo.model.form.monitor.MonitorCommand;
import cz.etnetera.reesmo.model.form.monitor.MonitorCommandValidator;
import cz.etnetera.reesmo.model.mongodb.monitoring.AnyMonitoring;
import cz.etnetera.reesmo.model.mongodb.monitoring.FlatlineMonitoring;
import cz.etnetera.reesmo.model.mongodb.monitoring.FrequencyMonitoring;
import cz.etnetera.reesmo.model.mongodb.monitoring.Monitoring;
import cz.etnetera.reesmo.model.mongodb.project.Project;
import cz.etnetera.reesmo.model.mongodb.user.Permission;
import cz.etnetera.reesmo.model.mongodb.view.View;
import cz.etnetera.reesmo.repository.mongodb.monitor.MonitorRepository;
import cz.etnetera.reesmo.repository.mongodb.project.ProjectRepository;
import cz.etnetera.reesmo.repository.mongodb.view.ViewRepository;
import cz.etnetera.reesmo.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@Controller
public class MonitorController implements MenuActivityController {

    @Autowired
    private UserManager userManager;

    @Autowired
    MonitorRepository monitorRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ViewRepository viewRepository;

    @Autowired
    Localizer localizer;

    @Override
    public String getActiveMenu() {
        return "projectMonitors";
    }

    @InitBinder(value = "monitorCommand")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(new MonitorCommandValidator());
    }

    @RequestMapping(value = "/monitor/{monitorId}", method = RequestMethod.GET)
    public String monitorDetail(@PathVariable String monitorId, Model model, Locale locale) {
        Monitoring monitor = monitorRepository.findOne(monitorId);
        ControllerModel.exists(monitor, Monitoring.class);
        Project project = projectRepository.findOne(monitor.getProjectId());
        ControllerModel.exists(project, Project.class);
        project.checkUserPermission(userManager.requireUser(), Permission.BASIC);
        View view = viewRepository.findOne(monitor.getViewId());
        ControllerModel.exists(view, View.class);
        MonitorDT monitorDT = new MonitorDT(monitor, localizer, locale);
        monitorDT.setViewName(view.getName());
        model.addAttribute("project", project);
        model.addAttribute("view", view);
        model.addAttribute("monitorDT", monitorDT);
        return "page/monitor/monitorDetail";
    }

    @RequestMapping(value = "/view/{viewId}/monitor/frequency/create", method = RequestMethod.GET)
    public String createFrequencyMonitorForm(@PathVariable String viewId, Model model) {
        View view = viewRepository.findOne(viewId);
        ControllerModel.exists(view, View.class);
        Project project = projectRepository.findOne(view.getProjectId());
        ControllerModel.exists(project, Project.class);
        project.checkUserPermission(userManager.requireUser(), Permission.ADMIN);
        model.addAttribute("monitorCommand", new MonitorCommand(TimeUnit.HOURS, 0, 0, true));
        model.addAttribute("project", project);
        model.addAttribute("view", view);
        return "page/monitor/frequencyMonitorCreate";
    }

    @RequestMapping(value = "/view/{viewId}/monitor/frequency/create", method = RequestMethod.POST)
    public String createFrequencyMonitor(Model model, @PathVariable String viewId, @Valid @ModelAttribute MonitorCommand monitorCommand, BindingResult result) {
        View view = viewRepository.findOne(viewId);
        ControllerModel.exists(view, View.class);
        Project project = projectRepository.findOne(view.getProjectId());
        ControllerModel.exists(project, Project.class);
        project.checkUserPermission(userManager.requireUser(), Permission.ADMIN);
        if (result.hasErrors()){
            model.addAttribute("project", project);
            model.addAttribute("view", view);
            return "page/monitor/frequencyMonitorCreate";
        }
        FrequencyMonitoring frequencyMonitoring = new FrequencyMonitoring();
        monitorCommand.toMonitor(frequencyMonitoring);
        frequencyMonitoring.setViewId(viewId);
        frequencyMonitoring.setProjectId(project.getId());
        frequencyMonitoring.setFrom(new Date());
        monitorRepository.save(frequencyMonitoring);
        return "redirect:/view/" + viewId;
    }

    //@RequestMapping(value = "/view/{viewId}/monitor/flatline/create", method = RequestMethod.GET)
    public String createFlatlineMonitorForm(@PathVariable String viewId, Model model) {
        View view = viewRepository.findOne(viewId);
        ControllerModel.exists(view, View.class);
        Project project = projectRepository.findOne(view.getProjectId());
        ControllerModel.exists(project, Project.class);
        project.checkUserPermission(userManager.requireUser(), Permission.ADMIN);
        model.addAttribute("monitorCommand", new MonitorCommand(TimeUnit.HOURS, 0, 0, true));
        model.addAttribute("project", project);
        model.addAttribute("view", view);
        return "page/monitor/flatlineMonitorCreate";
    }

    //@RequestMapping(value = "/view/{viewId}/monitor/flatline/create", method = RequestMethod.POST)
    public String createFlatlineMonitor(Model model, @PathVariable String viewId, @Valid @ModelAttribute MonitorCommand monitorCommand, BindingResult result) {
        View view = viewRepository.findOne(viewId);
        ControllerModel.exists(view, View.class);
        Project project = projectRepository.findOne(view.getProjectId());
        ControllerModel.exists(project, Project.class);
        project.checkUserPermission(userManager.requireUser(), Permission.ADMIN);
        if (result.hasErrors()){
            model.addAttribute("project", project);
            model.addAttribute("view", view);
            return "page/monitor/frequencyMonitorCreate";
        }
        FlatlineMonitoring flatlineMonitoring = new FlatlineMonitoring();
        monitorCommand.toMonitor(flatlineMonitoring);
        flatlineMonitoring.setViewId(viewId);
        flatlineMonitoring.setProjectId(project.getId());
        monitorRepository.save(flatlineMonitoring);
        return "redirect:/view/" + viewId;
    }

    @RequestMapping(value = "/view/{viewId}/monitor/any/create", method = RequestMethod.GET)
    public String createAnyMonitorForm(@PathVariable String viewId, Model model) {
        View view = viewRepository.findOne(viewId);
        ControllerModel.exists(view, View.class);
        Project project = projectRepository.findOne(view.getProjectId());
        ControllerModel.exists(project, Project.class);
        project.checkUserPermission(userManager.requireUser(), Permission.ADMIN);
        model.addAttribute("monitorCommand", new MonitorCommand(TimeUnit.HOURS, 0, 0, true));
        model.addAttribute("project", project);
        model.addAttribute("view", view);
        return "page/monitor/anyMonitorCreate";
    }

    @RequestMapping(value = "/view/{viewId}/monitor/any/create", method = RequestMethod.POST)
    public String createAnyMonitor(Model model, @PathVariable String viewId, @ModelAttribute MonitorCommand monitorCommand, BindingResult result) {
        View view = viewRepository.findOne(viewId);
        ControllerModel.exists(view, View.class);
        Project project = projectRepository.findOne(view.getProjectId());
        ControllerModel.exists(project, Project.class);
        project.checkUserPermission(userManager.requireUser(), Permission.ADMIN);
        if (result.hasErrors()){
            model.addAttribute("project", project);
            model.addAttribute("view", view);
            return "page/monitor/anyMonitorCreate";
        }
        AnyMonitoring anyMonitoring = new AnyMonitoring();
        anyMonitoring.setViewId(viewId);
        anyMonitoring.setProjectId(project.getId());
        anyMonitoring.setEnabled(monitorCommand.isEnabled());
        monitorRepository.save(anyMonitoring);
        return "redirect:/view/" + viewId;
    }

    @RequestMapping(value = "/monitor/delete/{monitorId}", method = RequestMethod.GET)
    public String deleteMonitor(Model model, @PathVariable String monitorId) {
        Monitoring monitor = monitorRepository.findOne(monitorId);
        ControllerModel.exists(monitor, Monitoring.class);
        Project project = projectRepository.findOne(monitor.getProjectId());
        ControllerModel.exists(project, Project.class);
        project.checkUserPermission(userManager.requireUser(), Permission.ADMIN);
        View view = viewRepository.findOne(monitor.getViewId());
        ControllerModel.exists(view, View.class);
        model.addAttribute("monitor", monitor);
        model.addAttribute("monitorType", monitor.getClass().getSimpleName());
        model.addAttribute("project", project);
        model.addAttribute("view", view);
        return "page/monitor/monitorDelete";
    }

    @RequestMapping(value = "/monitor/delete/{monitorId}", method = RequestMethod.POST)
    public String deleteMonitor(@PathVariable String monitorId) {
        Monitoring monitor = monitorRepository.findOne(monitorId);
        ControllerModel.exists(monitor, Monitoring.class);
        Project project = projectRepository.findOne(monitor.getProjectId());
        ControllerModel.exists(project, Project.class);
        project.checkUserPermission(userManager.requireUser(), Permission.ADMIN);
        View view = viewRepository.findOne(monitor.getViewId());
        ControllerModel.exists(view, View.class);
        monitorRepository.deleteMonitorAndNotifiers(monitorId);
        return "redirect:/view/" + view.getId();
    }

    @RequestMapping(value = "/monitor/edit/{monitorId}", method = RequestMethod.GET)
    public String editMonitor(Model model, @PathVariable String monitorId) {
        Monitoring monitor = monitorRepository.findOne(monitorId);
        ControllerModel.exists(monitor, Monitoring.class);
        Project project = projectRepository.findOne(monitor.getProjectId());
        ControllerModel.exists(project, Project.class);
        project.checkUserPermission(userManager.requireUser(), Permission.ADMIN);
        View view = viewRepository.findOne(monitor.getViewId());
        ControllerModel.exists(view, View.class);
        MonitorCommand monitorCommand = new MonitorCommand(monitor);
        model.addAttribute("monitorCommand", monitorCommand);
        model.addAttribute("project", project);
        model.addAttribute("view", view);
        return "page/monitor/monitorEdit";
    }

    @RequestMapping(value = "/monitor/edit/{monitorId}", method = RequestMethod.POST)
    public String editMonitor(Model model, @PathVariable String monitorId, @ModelAttribute MonitorCommand monitorCommand, BindingResult result) {
        Monitoring monitor = monitorRepository.findOne(monitorId);
        ControllerModel.exists(monitor, Monitoring.class);
        Project project = projectRepository.findOne(monitor.getProjectId());
        ControllerModel.exists(project, Project.class);
        project.checkUserPermission(userManager.requireUser(), Permission.ADMIN);
        if (result.hasErrors()){
            View view = viewRepository.findOne(monitor.getViewId());
            ControllerModel.exists(view, View.class);
            model.addAttribute("project", project);
            model.addAttribute("view", view);
            return "page/monitor/monitorEdit";
        }
        monitorCommand.toMonitor(monitor);
        monitorRepository.save(monitor);
        return "redirect:/monitor/" + monitorId;
    }

}
