package cz.etnetera.reesmo.controller.monitor;

import cz.etnetera.reesmo.message.Localizer;
import cz.etnetera.reesmo.model.datatables.monitor.MonitorDT;
import cz.etnetera.reesmo.model.form.monitor.MonitorCommand;
import cz.etnetera.reesmo.model.form.monitor.MonitorCommandValidator;
import cz.etnetera.reesmo.model.mongodb.monitoring.AnyMonitoring;
import cz.etnetera.reesmo.model.mongodb.monitoring.FlatlineMonitoring;
import cz.etnetera.reesmo.model.mongodb.monitoring.FrequencyMonitoring;
import cz.etnetera.reesmo.model.mongodb.monitoring.Monitoring;
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

import javax.validation.Valid;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@Controller
public class MonitorController {


    @Autowired
    MonitorRepository monitorRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    ViewRepository viewRepository;

    @Autowired
    Localizer localizer;

    @InitBinder(value = "monitorCommand")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(new MonitorCommandValidator());
    }


    @RequestMapping(value = "/monitor/{monitorId}", method = RequestMethod.GET)
    public String monitorDetail(@PathVariable String monitorId, Model model, Locale locale) {
        Monitoring monitor = monitorRepository.findOne(monitorId);
        View view = viewRepository.findOne(monitor.getViewId());
        Project project = projectRepository.findOne(view.getProjectId());
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
        Project project = projectRepository.findOne(view.getProjectId());
        model.addAttribute("monitorCommand", new MonitorCommand(TimeUnit.HOURS, 0, 0, true));
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
        monitorRepository.save(frequencyMonitoring);
        return "redirect:/view/" + viewId;
    }

    @RequestMapping(value = "/view/{viewId}/monitor/flatline/create", method = RequestMethod.GET)
    public String createFlatlineMonitorForm(@PathVariable String viewId, Model model) {
        View view = viewRepository.findOne(viewId);
        Project project = projectRepository.findOne(view.getProjectId());
        model.addAttribute("monitorCommand", new MonitorCommand(TimeUnit.HOURS, 0, 0, true));
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
        monitorRepository.save(flatlineMonitoring);
        return "redirect:/view/" + viewId;
    }

    @RequestMapping(value = "/view/{viewId}/monitor/any/create", method = RequestMethod.GET)
    public String createAnyMonitorForm(@PathVariable String viewId, Model model) {
        View view = viewRepository.findOne(viewId);
        Project project = projectRepository.findOne(view.getProjectId());
        model.addAttribute("monitorCommand", new MonitorCommand(TimeUnit.HOURS, 0, 0, true));
        model.addAttribute("project", project);
        model.addAttribute("view", view);
        return "page/monitor/anyMonitorCreate";
    }

    @RequestMapping(value = "/view/{viewId}/monitor/any/create", method = RequestMethod.POST)
    public String createAnyMonitor(Model model, @PathVariable String viewId, @ModelAttribute MonitorCommand monitorCommand) {
        AnyMonitoring anyMonitoring = new AnyMonitoring();
        anyMonitoring.setViewId(viewId);
        anyMonitoring.setEnabled(monitorCommand.isEnabled());
        monitorRepository.save(anyMonitoring);
        return "redirect:/view/" + viewId;
    }

    @RequestMapping(value = "/monitor/delete/{monitorId}", method = RequestMethod.GET)
    public String deleteMonitor(Model model, @PathVariable String monitorId) {
        Monitoring monitor = monitorRepository.findOne(monitorId);
        View view = viewRepository.findOne(monitor.getViewId());
        Project project = projectRepository.findOne(view.getProjectId());
        model.addAttribute("monitor", monitor);
        model.addAttribute("monitorType", monitor.getClass().getSimpleName());
        model.addAttribute("project", project);
        model.addAttribute("view", view);
        return "page/monitor/monitorDelete";
    }

    @RequestMapping(value = "/monitor/delete/{monitorId}", method = RequestMethod.POST)
    public String deleteMonitor(@PathVariable String monitorId) {
        Monitoring monitor = monitorRepository.findOne(monitorId);
        View view = viewRepository.findOne(monitor.getViewId());
        monitorRepository.deleteMonitorAndNotifiers(monitorId);
        return "redirect:/view/" + view.getId();
    }

    @RequestMapping(value = "/monitor/edit/{monitorId}", method = RequestMethod.GET)
    public String editMonitor(Model model, @PathVariable String monitorId) {
        Monitoring monitor = monitorRepository.findOne(monitorId);
        View view = viewRepository.findOne(monitor.getViewId());
        Project project = projectRepository.findOne(view.getProjectId());
        MonitorCommand monitorCommand = new MonitorCommand(monitor);
        model.addAttribute("monitorCommand", monitorCommand);
        model.addAttribute("project", project);
        model.addAttribute("view", view);
        return "page/monitor/monitorEdit";
    }

    @RequestMapping(value = "/monitor/edit/{monitorId}", method = RequestMethod.POST)
    public String editMonitor(@PathVariable String monitorId, @ModelAttribute MonitorCommand monitorCommand) {
        Monitoring monitor = monitorRepository.findOne(monitorId);
        monitorCommand.toMonitor(monitor);
        monitorRepository.save(monitor);
        return "redirect:/monitor/" + monitorId;
    }


}
