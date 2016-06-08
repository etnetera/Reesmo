package cz.etnetera.reesmo.repository.mongodb.monitor;

import com.github.dandelion.datatables.core.ajax.DataSet;
import cz.etnetera.reesmo.model.datatables.monitor.MonitorDT;
import cz.etnetera.reesmo.model.mongodb.monitoring.Monitoring;
import cz.etnetera.reesmo.model.mongodb.project.Project;
import cz.etnetera.reesmo.model.mongodb.view.View;

import java.util.List;

public interface MonitorRepositoryCustom {

    public List<Monitoring> findMonitorsByView(String viewId);

    public List<Monitoring> findMonitorsByView(View view);

    public List<Monitoring> findMonitorsByProject(String projectId);

    public List<Monitoring> findMonitorsByProject(Project project);

    public DataSet<MonitorDT> findProjectMonitors(String projectId);

    public DataSet<MonitorDT> findProjectMonitors(Project project);

    public DataSet<MonitorDT> findViewMonitors(String viewId);

    public DataSet<MonitorDT> findViewMonitors(View view);


}
