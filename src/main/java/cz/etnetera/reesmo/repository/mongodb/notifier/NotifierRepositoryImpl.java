package cz.etnetera.reesmo.repository.mongodb.notifier;

import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import cz.etnetera.reesmo.model.datatables.notifier.NotifierDT;
import cz.etnetera.reesmo.model.mongodb.notifier.Notifier;
import cz.etnetera.reesmo.repository.mongodb.MongoDatatables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.stream.Collectors;

public class NotifierRepositoryImpl implements NotifierRepositoryCustom {

    @Autowired
    private MongoOperations mongoTemplate;


    @Override
    public DataSet<NotifierDT> findWithDatatablesCriterias(DatatablesCriterias criterias, String monitoringId) {
        DataSet<Notifier> notifiers = findNotifiersWithDatatablesCriterias(criterias, monitoringId);

        return new DataSet<NotifierDT>(
                notifiers.getRows().stream().map(NotifierDT::new).collect(Collectors.toList()),
                notifiers.getTotalRecords(), notifiers.getTotalDisplayRecords());
    }

    @Override
    public List<Notifier> findAllByMonitoring(String monitoringId) {
        return mongoTemplate.find(Query.query(Criteria.where("monitorId").is(monitoringId)), Notifier.class);
    }

    private DataSet<Notifier> findNotifiersWithDatatablesCriterias(DatatablesCriterias criterias, String monitoringId) {
        Criteria allCrit = Criteria.where("monitorId").is(monitoringId);
        Criteria crit = MongoDatatables.getCriteria(criterias, allCrit);

        Long count = mongoTemplate.count(Query.query(allCrit), Notifier.class);
        Long countFiltered = mongoTemplate.count(Query.query(crit), Notifier.class);

        Query query = Query.query(crit);
        MongoDatatables.sortQuery(query, criterias);
        MongoDatatables.paginateQuery(query, criterias);

        List<Notifier> notifiers = mongoTemplate.find(query, Notifier.class);

        return new DataSet<Notifier>(notifiers, count, countFiltered);
    }
}
