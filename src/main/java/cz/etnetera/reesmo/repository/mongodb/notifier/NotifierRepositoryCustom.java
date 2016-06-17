package cz.etnetera.reesmo.repository.mongodb.notifier;


import com.github.dandelion.datatables.core.ajax.DataSet;
import cz.etnetera.reesmo.model.datatables.notifier.NotifierDT;

public interface NotifierRepositoryCustom {

    public DataSet<NotifierDT> findDSByMonitor(String monitorId);

}
