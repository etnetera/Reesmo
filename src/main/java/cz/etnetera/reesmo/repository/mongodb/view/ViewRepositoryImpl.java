package cz.etnetera.reesmo.repository.mongodb.view;

import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import cz.etnetera.reesmo.model.datatables.view.ViewDT;
import cz.etnetera.reesmo.model.mongodb.monitoring.Monitoring;
import cz.etnetera.reesmo.model.mongodb.view.View;
import cz.etnetera.reesmo.repository.mongodb.MongoDatatables;
import cz.etnetera.reesmo.repository.mongodb.monitor.MonitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.stream.Collectors;

/**
 * View repository custom method implementation
 */
public class ViewRepositoryImpl implements ViewRepositoryCustom {

    @Autowired
    private MongoOperations mongoTemplate;

    @Autowired
    private MonitorRepository monitorRepository;



    @Override
	public List<View> findByProject(String projectId) {
		return mongoTemplate.find(Query.query(Criteria.where("projectId").is(projectId)), View.class, "view");
	}
    
    @Override
    public DataSet<ViewDT> findViewsForProjectWithCriterias(DatatablesCriterias criterias, String projectId) {
        DataSet<View> views = findViewsWithDatatablesCriterias(criterias, projectId);

        return new DataSet<ViewDT>(
                views.getRows().stream().map(p -> new ViewDT(p)).collect(Collectors.toList()),
                views.getTotalRecords(), views.getTotalDisplayRecords());
    }

    @Override
    public void deleteViewAndMonitors(String viewId) {
        List<Monitoring> monitors = mongoTemplate.find(Query.query(Criteria.where("viewId").is(viewId)), Monitoring.class, "monitoring");
        monitors.forEach(monitoring -> monitorRepository.deleteMonitorAndNotifiers(monitoring.getId()));
        mongoTemplate.remove(Query.query(Criteria.where("_id").is(viewId)), "view");
    }

    private DataSet<View> findViewsWithDatatablesCriterias(DatatablesCriterias criterias, String projectId) {
        Criteria allCrit = Criteria.where("projectId").is(projectId);

        Criteria crit = MongoDatatables.getCriteria(criterias, allCrit);

        Long count = mongoTemplate.count(Query.query(allCrit), "view");
        Long countFiltered = mongoTemplate.count(Query.query(crit), "view");

        Query query = Query.query(crit);
        MongoDatatables.sortQuery(query, criterias);
        MongoDatatables.paginateQuery(query, criterias);

        List<View> views = mongoTemplate.find(query, View.class, "view");

        return new DataSet<View>(views, count, countFiltered);

    }

}
