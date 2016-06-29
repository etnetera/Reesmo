package cz.etnetera.reesmo.repository.mongodb.notifier;


import com.github.dandelion.datatables.core.ajax.DataSet;
import com.github.dandelion.datatables.core.ajax.DatatablesCriterias;
import cz.etnetera.reesmo.model.datatables.notifier.NotifierDT;
import cz.etnetera.reesmo.model.mongodb.notifier.Notifier;

import java.util.List;

public interface NotifierRepositoryCustom {

    public DataSet<NotifierDT> findWithDatatablesCriterias(DatatablesCriterias criterias, String monitoringId);

    public List<Notifier> findAllByMonitoring(String monitorId);

}
