package cz.etnetera.reesmo.repository.mongodb.view;

import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import cz.etnetera.reesmo.model.datatables.view.ViewDT;
import cz.etnetera.reesmo.model.mongodb.user.User;
import cz.etnetera.reesmo.model.mongodb.view.View;
import cz.etnetera.reesmo.repository.mongodb.MongoDatatables;
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

    @Override
    public DataSet<ViewDT> findViewsForProjectWithCriterias(DatatablesCriterias criterias, String projectId) {
        DataSet<View> views = findViewsWithDatatablesCriterias(criterias, projectId);

        return new DataSet<ViewDT>(
                views.getRows().stream().map(p -> new ViewDT(p)).collect(Collectors.toList()),
                views.getTotalRecords(), views.getTotalDisplayRecords());
    }


    private DataSet<View> findViewsWithDatatablesCriterias(DatatablesCriterias criterias, String projectId) {
        Criteria allCrit = Criteria.where("_projectId").is(projectId);

        Criteria crit = MongoDatatables.getCriteria(criterias, allCrit);

        Long count = mongoTemplate.count(Query.query(allCrit), User.class);
        Long countFiltered = mongoTemplate.count(Query.query(crit), User.class);

        Query query = Query.query(crit);
        MongoDatatables.sortQuery(query, criterias);
        MongoDatatables.paginateQuery(query, criterias);

        List<View> views = mongoTemplate.find(query, View.class);

        return new DataSet<View>(views, count, countFiltered);

    }
}
