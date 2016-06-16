package cz.etnetera.reesmo.repository.mongodb.notifier;

import com.github.dandelion.datatables.core.ajax.DataSet;
import cz.etnetera.reesmo.model.datatables.notifier.NotifierDT;
import cz.etnetera.reesmo.notifier.Notifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.List;

public class NotifierRepositoryImpl implements NotifierRepositoryCustom {

    @Autowired
    private MongoOperations mongoTemplate;


    @Override
    public DataSet<NotifierDT> findDSByMonitor(String monitorId) {
        List<Notifier> notifiers = mongoTemplate.find(Query.query(Criteria.where("monitorId").is(monitorId)), Notifier.class, "notifier");
        List<NotifierDT> notifierDTList = new ArrayList<>();
        notifiers.forEach(notifier -> notifierDTList.add(new NotifierDT(notifier)));
        Long all = Long.valueOf(notifierDTList.size());
        DataSet<NotifierDT> notifierDataSet = new DataSet<>(notifierDTList, all, all);
        return notifierDataSet;
    }
}
