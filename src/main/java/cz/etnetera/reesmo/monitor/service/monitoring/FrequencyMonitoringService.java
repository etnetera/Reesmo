package cz.etnetera.reesmo.monitor.service.monitoring;

import cz.etnetera.reesmo.list.ListSorter;
import cz.etnetera.reesmo.list.PageableListModifier;
import cz.etnetera.reesmo.model.elasticsearch.result.Result;
import cz.etnetera.reesmo.model.mongodb.monitoring.FrequencyMonitoring;
import cz.etnetera.reesmo.model.mongodb.monitoring.Monitoring;
import cz.etnetera.reesmo.model.mongodb.view.View;
import cz.etnetera.reesmo.monitor.MonitoringManager;
import cz.etnetera.reesmo.repository.mongodb.monitor.MonitorRepository;
import cz.etnetera.reesmo.repository.mongodb.view.ViewRepository;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.RangeFilterBuilder;
import org.elasticsearch.index.query.TermFilterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class FrequencyMonitoringService implements MonitoringService {

    @Autowired
    private MonitoringManager monitoringManager;

    @Autowired
    private ViewRepository viewRepository;

    @Autowired
    private MonitorRepository monitorRepository;


    @Autowired
    private ElasticsearchOperations template;

    @PostConstruct
    private void init() {
        monitoringManager.registerMonitoringService(this);
    }

    @Override
    public boolean supportsMonitoring(Monitoring monitoring) {
        return monitoring instanceof FrequencyMonitoring;
    }

    @Override
    public boolean shouldNotify(Monitoring monitoring, Result result) {
        FrequencyMonitoring monitor = (FrequencyMonitoring) monitoring;
        View view = viewRepository.findOne(monitor.getViewId());
        PageableListModifier modifier = new PageableListModifier(0, Integer.MAX_VALUE).join(view.getModifier());

        ListSorter listSorter = new ListSorter();
        listSorter.setField("endedAt");
        listSorter.setWay("asc");
        List<ListSorter> listSorters = Arrays.asList(listSorter);
        modifier.setSorters(listSorters);

        List<Result> queryResults = template.queryForList(createSearchBuilderFromModifier(modifier, modifier.getFilterBuilder(
                        new BoolFilterBuilder().must(new TermFilterBuilder("projectId", view.getProjectId())).cache(true),
                        new BoolFilterBuilder().must(new RangeFilterBuilder("endedAt").gt(monitor.getFrom().toInstant().toEpochMilli())).cache(true)))
                .build(), Result.class);

        long monitorFrame = monitor.getTimeUnit().toMillis(monitor.getNumberOfTimeUnits());
        for (int i = 0; i <= queryResults.size() - monitor.getHits(); i++) {
            Date bottomResultDate = queryResults.get(i).getEndedAt();
            Date deadline = Date.from(bottomResultDate.toInstant().plusMillis(monitorFrame));
            Date upperResultDate = queryResults.get(i + monitor.getHits() - 1).getEndedAt();
            boolean shouldNotify = deadline.after(upperResultDate);

            if (shouldNotify) {
                monitor.setFrom(upperResultDate);
                monitorRepository.save(monitor);
                return true;
            }
            monitor.setFrom(bottomResultDate);
            monitorRepository.save(monitor);
        }
        return false;
    }


    private NativeSearchQueryBuilder createSearchBuilderFromModifier(PageableListModifier modifier, FilterBuilder filterBuilder) {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder().withTypes("result")
                .withFilter(filterBuilder == null ? modifier.getFilterBuilder() : filterBuilder)
                .withPageable(modifier.getPageable());
        modifier.getSortBuilders().forEach(sb -> builder.withSort(sb));
        return builder;
    }

}
