package cz.etnetera.reesmo.repository.mongodb.monitor;

import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import cz.etnetera.reesmo.model.datatables.monitor.MonitorDT;
import cz.etnetera.reesmo.model.mongodb.monitoring.Monitoring;
import cz.etnetera.reesmo.model.mongodb.project.Project;
import cz.etnetera.reesmo.model.mongodb.view.View;

import java.util.List;
import java.util.Locale;

public interface MonitorRepositoryCustom {

    public List<Monitoring> findMonitorsForView(String viewId);

    public List<Monitoring> findMonitorsForView(View view);

    public List<Monitoring> findMonitorsForProject(String projectId);

    public List<Monitoring> findMonitorsForProject(Project project);

    public DataSet<MonitorDT> findMonitorsDSForProject(String projectId, DatatablesCriterias criterias, Locale locale);

    public DataSet<MonitorDT> findMonitorsDSForProject(Project project, DatatablesCriterias criterias, Locale locale);

    public DataSet<MonitorDT> findMonitorsDSForView(String viewId, DatatablesCriterias criterias, Locale locale);

    public DataSet<MonitorDT> findMonitorsDSForView(View view, DatatablesCriterias criterias, Locale locale);

    void deleteMonitorAndNotifiers(String monitorId);
}
