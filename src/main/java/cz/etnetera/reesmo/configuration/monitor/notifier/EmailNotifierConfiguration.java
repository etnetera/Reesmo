package cz.etnetera.reesmo.configuration.monitor.notifier;

import cz.etnetera.reesmo.monitor.service.notifier.EmailNotifierService;
import org.springframework.context.annotation.Configuration;

@Configuration
class EmailNotifierConfiguration {

    //@Bean
    public EmailNotifierService emailNotifierService() {
        return new EmailNotifierService();
    }

}