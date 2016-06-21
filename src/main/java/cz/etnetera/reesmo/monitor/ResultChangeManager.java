package cz.etnetera.reesmo.monitor;

import cz.etnetera.reesmo.model.mongodb.resultchange.ResultChange;
import cz.etnetera.reesmo.repository.mongodb.resultchange.ResultChangeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ResultChangeManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResultChangeManager.class);

    private static final int RELOAD_EVERY_MS = 10000;

    @Autowired
    private ResultChangeRepository resultChangeRepository;

    @Scheduled(fixedRate = RELOAD_EVERY_MS, initialDelay = 10000)
    private void refresh() {
        LOGGER.info("Result changes refreshed at " + new Date());
        resultChangeRepository.findAll().forEach(this::resolveChange);
    }

    private void resolveChange(ResultChange change) {
        // TODO - resolve it
        resultChangeRepository.delete(change);
    }

}
