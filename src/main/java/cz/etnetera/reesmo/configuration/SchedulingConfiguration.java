package cz.etnetera.reesmo.configuration;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@EnableScheduling
@Configuration
public class SchedulingConfiguration implements SchedulingConfigurer {

    @Value("${scheduler.pool.size}")
    private Integer poolSize;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskScheduler());
    }

    @Bean(destroyMethod = "shutdownNow")
    public ExecutorService taskScheduler() {
        return Executors.newScheduledThreadPool(poolSize, new ThreadFactoryBuilder()
                .setNameFormat("reesmo-tasksched-bus-%s").build());
    }

}
