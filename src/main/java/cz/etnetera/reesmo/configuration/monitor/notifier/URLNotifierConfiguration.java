package cz.etnetera.reesmo.configuration.monitor.notifier;

import cz.etnetera.reesmo.monitor.service.notifier.URLNotifierService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class URLNotifierConfiguration {

    @Bean
    public URLNotifierService urlNotifierService() {
        return new URLNotifierService();
    }

}