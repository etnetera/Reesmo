package cz.etnetera.reesmo.repository.mongodb.monitor;

import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import cz.etnetera.reesmo.message.Localizer;
import cz.etnetera.reesmo.model.datatables.monitor.MonitorDT;
import cz.etnetera.reesmo.model.mongodb.monitoring.Monitoring;
import cz.etnetera.reesmo.model.mongodb.project.Project;
import cz.etnetera.reesmo.model.mongodb.view.View;
import cz.etnetera.reesmo.repository.mongodb.MongoDatatables;
import cz.etnetera.reesmo.repository.mongodb.view.ViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class MonitorRepositoryImpl implements  MonitorRepositoryCustom{

    @Autowired
    private MongoOperations mongoTemplate;

    @Autowired
    private ViewRepository viewRepository;

    @Autowired
    private Localizer localizer;

    @Override
    public List<Monitoring> findMonitorsForView(String viewId) {
        return mongoTemplate.find(Query.query(Criteria.where("viewId").is(viewId)), Monitoring.class, "monitoring");
    }

    @Override
    public List<Monitoring> findMonitorsForProject(String projectId) {
        List<String> viewIds = viewRepository.findByProject(projectId).stream().map(view -> view.getId())
                .collect(Collectors.toList());
        ArrayList<Monitoring> monitors = new ArrayList<>();
        viewIds.forEach(viewId -> monitors.addAll(findMonitorsForView(viewId)));
        return monitors;
    }

    @Override
    public List<Monitoring> findMonitorsForProject(Project project) {
        return findMonitorsForProject(project.getId());
    }

    @Override
    public DataSet<MonitorDT> findMonitorsDSForProject(String projectId, DatatablesCriterias criterias, Locale locale) {
        List<Monitoring> monitorsForProject = findMonitorsForProject(projectId);
        return findMonitorsWithDatatablesCriterias(monitorsForProject, criterias, locale);
    }

    @Override
    public DataSet<MonitorDT> findMonitorsDSForProject(Project project, DatatablesCriterias criterias, Locale locale) {
        return findMonitorsDSForProject(project.getId(), criterias, locale);
    }

    @Override
    public List<Monitoring> findMonitorsForView(View view) {
        return findMonitorsForView(view.getId());
    }

    @Override
    public DataSet<MonitorDT> findMonitorsDSForView(String viewId, DatatablesCriterias criterias, Locale locale) {
        List<Monitoring> monitorsForView = findMonitorsForView(viewId);
        return findMonitorsWithDatatablesCriterias(monitorsForView, criterias, locale);
    }

    @Override
    public DataSet<MonitorDT> findMonitorsDSForView(View view, DatatablesCriterias criterias, Locale locale) {
        return findMonitorsDSForView(view.getId(), criterias, locale);
    }

    private DataSet<MonitorDT> findMonitorsWithDatatablesCriterias(Collection<Monitoring> monitors, DatatablesCriterias criterias, Locale locale) {
        if (monitors.isEmpty()) {
            return new DataSet<MonitorDT>(new ArrayList<>(), 0L, 0L);
        }

        Criteria allCrit = Criteria.where("_id").in(monitors.stream().map(monitoring -> monitoring.getId()).collect(Collectors.toList()));
        Criteria crit = MongoDatatables.getCriteria(criterias, allCrit);

        Long count = mongoTemplate.count(Query.query(allCrit), "monitoring");
        Long displayed = mongoTemplate.count(Query.query(crit), "monitoring");

        Query query = Query.query(crit);
        MongoDatatables.sortQuery(query, criterias);
        MongoDatatables.paginateQuery(query, criterias);

        List<Monitoring> monitorings = mongoTemplate.find(query, Monitoring.class, "monitoring");

        return getDataSetForList(monitorings , count, displayed, locale);
    }

    private DataSet<MonitorDT> getDataSetForList(List<Monitoring> monitors, long count, long displayed, Locale locale) {
        List<MonitorDT> monitorDTs = monitors.stream().map(monitoring -> {
            MonitorDT monitorDT = new MonitorDT(monitoring, localizer, locale);
            monitorDT.setViewName(viewRepository.findOne(monitorDT.getViewIdDT()).getName());
            return monitorDT;
        }).collect(Collectors.toList());
        return new DataSet<MonitorDT>(monitorDTs, count, displayed);
    }


    @Override
    public void deleteMonitorAndNotifiers(String monitorId) {
        mongoTemplate.remove(Query.query(Criteria.where("monitorId").is(monitorId)), "notifier");
        mongoTemplate.remove(Query.query(Criteria.where("_id").is(monitorId)), "monitoring");
    }


}
