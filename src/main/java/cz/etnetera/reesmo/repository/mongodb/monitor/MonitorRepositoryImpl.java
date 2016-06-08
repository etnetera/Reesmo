package cz.etnetera.reesmo.repository.mongodb.monitor;

import com.github.dandelion.datatables.core.ajax.DataSet;
import cz.etnetera.reesmo.model.datatables.monitor.MonitorDT;
import cz.etnetera.reesmo.model.mongodb.monitoring.AnyMonitoring;
import cz.etnetera.reesmo.model.mongodb.monitoring.FlatlineMonitoring;
import cz.etnetera.reesmo.model.mongodb.monitoring.FrequencyMonitoring;
import cz.etnetera.reesmo.model.mongodb.monitoring.Monitoring;
import cz.etnetera.reesmo.model.mongodb.project.Project;
import cz.etnetera.reesmo.model.mongodb.view.View;
import cz.etnetera.reesmo.repository.mongodb.view.ViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MonitorRepositoryImpl implements  MonitorRepositoryCustom{

    @Autowired
    private MongoOperations mongoTemplate;

    @Autowired
    private ViewRepository viewRepository;


    @Override
    public List<Monitoring> findMonitorsByView(String viewId) {
        ArrayList<Monitoring> monitors = new ArrayList<>();
        monitors.addAll(mongoTemplate.find(Query.query(Criteria.where("viewId").is(viewId).where("_class").is("cz.etnetera.reesmo.model.mongodb.monitoring.FrequencyMonitoring")), FrequencyMonitoring.class));
        monitors.addAll(mongoTemplate.find(Query.query(Criteria.where("viewId").is(viewId).where("_class").is("cz.etnetera.reesmo.model.mongodb.monitoring.FlatlineMonitoring")), FlatlineMonitoring.class));
        monitors.addAll(mongoTemplate.find(Query.query(Criteria.where("viewId").is(viewId).where("_class").is("cz.etnetera.reesmo.model.mongodb.monitoring.AnyMonitoring")), AnyMonitoring.class));
        return monitors;
    }



    @Override
    public List<Monitoring> findMonitorsByView(View view) {
        return findMonitorsByView(view.getId());
    }

    @Override
    public List<Monitoring> findMonitorsByProject(String projectId) {
        return null;
    }

    @Override
    public List<Monitoring> findMonitorsByProject(Project project) {
        return null;
    }

    @Override
    public DataSet<MonitorDT> findProjectMonitors(String projectId) {
        return null;
    }

    @Override
    public DataSet<MonitorDT> findProjectMonitors(Project project) {
        return null;
    }

    @Override
    public DataSet<MonitorDT> findViewMonitors(String viewId) {
        List<MonitorDT> monitorDTs = findMonitorsByView(viewId).stream().map(monitoring -> {
            MonitorDT monitorDT = new MonitorDT(monitoring);
            monitorDT.setViewName(viewRepository.findOne(monitorDT.getViewIdDT()).getName());
            return monitorDT;
        }).collect(Collectors.toList());
        Long count = (long) monitorDTs.size();
        Long displayed = (long) monitorDTs.size();
        return new DataSet<MonitorDT>(monitorDTs, count, displayed);
    }

    @Override
    public DataSet<MonitorDT> findViewMonitors(View view) {
        return findViewMonitors(view.getId());
    }
}
