package cz.etnetera.reesmo.controller.notifier;

import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import com.github.dandelion.datatables.core.ajax.DatatablesResponse;
import cz.etnetera.reesmo.controller.MenuActivityController;
import cz.etnetera.reesmo.model.datatables.notifier.NotifierDT;
import cz.etnetera.reesmo.model.form.notifier.NotifierCommand;
import cz.etnetera.reesmo.model.mongodb.monitoring.Monitoring;
import cz.etnetera.reesmo.model.mongodb.notifier.EmailNotifier;
import cz.etnetera.reesmo.model.mongodb.notifier.Notifier;
import cz.etnetera.reesmo.model.mongodb.notifier.URLNotifier;
import cz.etnetera.reesmo.model.mongodb.project.Project;
import cz.etnetera.reesmo.model.mongodb.view.View;
import cz.etnetera.reesmo.repository.mongodb.monitor.MonitorRepository;
import cz.etnetera.reesmo.repository.mongodb.notifier.NotifierRepository;
import cz.etnetera.reesmo.repository.mongodb.project.ProjectRepository;
import cz.etnetera.reesmo.repository.mongodb.view.ViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class NotifierController implements MenuActivityController {

    @Autowired
    private MonitorRepository monitorRepository;

    @Autowired
    private ViewRepository viewRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private NotifierRepository notifierRepository;

    @Override
    public String getActiveMenu() {
        return "projectMonitors";
    }

    //@RequestMapping(value = "/monitor/{monitorId}/notifier/email/create", method = RequestMethod.GET)
    public String createEmailNotifier(@PathVariable String monitorId, Model model) {
        Monitoring monitor = monitorRepository.findOne(monitorId);
        View view = viewRepository.findOne(monitor.getViewId());
        Project project = projectRepository.findOne(view.getProjectId());

        model.addAttribute("notifierCommand", new NotifierCommand());
        model.addAttribute("project", project);
        model.addAttribute("view", view);
        model.addAttribute("monitor", monitor);
        return "page/notifier/emailNotifierCreate";
    }

    //@RequestMapping(value = "/monitor/{monitorId}/notifier/email/create", method = RequestMethod.POST)
    public String createEmailNotifier(Model model, @PathVariable String monitorId, @Valid @ModelAttribute NotifierCommand notifierCommand, BindingResult result) {
        Monitoring monitor = monitorRepository.findOne(monitorId);
        if (result.hasErrors()){
            View view = viewRepository.findOne(monitor.getViewId());
            Project project = projectRepository.findOne(view.getProjectId());
            model.addAttribute("project", project);
            model.addAttribute("monitor", monitor);
            model.addAttribute("view", view);
            return "page/notifier/emailNotifierCreate";
        }
        EmailNotifier emailNotifier = new EmailNotifier();
        notifierCommand.toNotifier(emailNotifier);
        emailNotifier.setMonitorId(monitorId);
        notifierRepository.save(emailNotifier);
        return "redirect:/monitor/" + monitor.getId();
    }


    @RequestMapping(value = "/monitor/{monitorId}/notifier/url/create", method = RequestMethod.GET)
    public String createUrlNotifier(@PathVariable String monitorId, Model model) {
        Monitoring monitor = monitorRepository.findOne(monitorId);
        View view = viewRepository.findOne(monitor.getViewId());
        Project project = projectRepository.findOne(view.getProjectId());

        model.addAttribute("notifierCommand", new NotifierCommand());
        model.addAttribute("project", project);
        model.addAttribute("view", view);
        model.addAttribute("monitor", monitor);
        return "page/notifier/urlNotifierCreate";
    }

    @RequestMapping(value = "/monitor/{monitorId}/notifier/url/create", method = RequestMethod.POST)
    public String createUrlNotifier(Model model, @PathVariable String monitorId, @Valid @ModelAttribute NotifierCommand notifierCommand, BindingResult result) {
        Monitoring monitor = monitorRepository.findOne(monitorId);
        if (result.hasErrors()){
            View view = viewRepository.findOne(monitor.getViewId());
            Project project = projectRepository.findOne(view.getProjectId());
            model.addAttribute("project", project);
            model.addAttribute("monitor", monitor);
            model.addAttribute("view", view);
            return "page/notifier/emailNotifierCreate";
        }
        URLNotifier emailNotifier = new URLNotifier();
        notifierCommand.toNotifier(emailNotifier);
        emailNotifier.setMonitorId(monitorId);
        notifierRepository.save(emailNotifier);
        return "redirect:/monitor/" + monitor.getId();
    }


    @RequestMapping(value = "/notifier/{notifierId}/delete", method = RequestMethod.GET)
    public String deleteNotifier(@PathVariable String notifierId, Model model) {
        Notifier notifier = notifierRepository.findOne(notifierId);
        Monitoring monitor = monitorRepository.findOne(notifier.getMonitorId());
        View view = viewRepository.findOne(monitor.getViewId());
        Project project = projectRepository.findOne(view.getProjectId());
        model.addAttribute("notifier", notifier);
        model.addAttribute("monitor", monitor);
        model.addAttribute("view", view);
        model.addAttribute("project", project);
        return "page/notifier/notifierDelete";
    }

    @RequestMapping(value = "/notifier/{notifierId}/delete", method = RequestMethod.POST)
    public String deleteNotifier(@PathVariable String notifierId) {
        Notifier notifier = notifierRepository.findOne(notifierId);
        notifierRepository.delete(notifierId);
        return "redirect:/monitor/" + notifier.getMonitorId();
    }

    @RequestMapping(value = "/notifier/{notifierId}/edit", method = RequestMethod.GET)
    public String editNotifier(@PathVariable String notifierId, Model model) {
        Notifier notifier = notifierRepository.findOne(notifierId);
        Monitoring monitor = monitorRepository.findOne(notifier.getMonitorId());
        View view = viewRepository.findOne(monitor.getViewId());
        Project project = projectRepository.findOne(view.getProjectId());

        model.addAttribute("notifierCommand", new NotifierCommand(notifier));
        model.addAttribute("monitor", monitor);
        model.addAttribute("view", view);
        model.addAttribute("project", project);
        return "page/notifier/notifierEdit";
    }

    @RequestMapping(value = "/notifier/{notifierId}/edit", method = RequestMethod.POST)
    public String editNotifier(@PathVariable String notifierId, @ModelAttribute NotifierCommand notifierCommand) {
        Notifier notifier = notifierRepository.findOne(notifierId);
        notifierCommand.toNotifier(notifier);
        notifierRepository.save(notifier);
        return "redirect:/monitor/" + notifier.getMonitorId();
    }

    @RequestMapping(value = "/dt/monitor/notifiers/{monitorId}")
    public @ResponseBody
    DatatablesResponse<NotifierDT> findAllNotifiersForMonitor(@PathVariable String monitorId, HttpServletRequest request) throws Exception {
        DatatablesCriterias criterias = DatatablesCriterias.getFromRequest(request);
        DataSet<NotifierDT> notifiers = notifierRepository.findWithDatatablesCriterias(criterias, monitorId);
        return DatatablesResponse.build(notifiers, criterias);
    }

}
