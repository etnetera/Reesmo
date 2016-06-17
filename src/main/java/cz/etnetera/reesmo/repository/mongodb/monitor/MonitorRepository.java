package cz.etnetera.reesmo.repository.mongodb.monitor;

import cz.etnetera.reesmo.model.mongodb.monitoring.Monitoring;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitorRepository extends MongoRepository<Monitoring, String>, MonitorRepositoryCustom{

}
