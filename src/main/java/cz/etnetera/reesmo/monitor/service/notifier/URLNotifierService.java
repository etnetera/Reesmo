package cz.etnetera.reesmo.monitor.service.notifier;

import cz.etnetera.reesmo.model.elasticsearch.result.Result;
import cz.etnetera.reesmo.model.mongodb.notifier.Notifier;
import cz.etnetera.reesmo.model.mongodb.notifier.URLNotifier;
import cz.etnetera.reesmo.monitor.MonitoringManager;
import cz.etnetera.reesmo.monitor.MonitoringNotification;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class URLNotifierService implements NotifierService {

    private static final Logger LOGGER = LoggerFactory.getLogger(URLNotifierService.class);

    @Autowired
    private MonitoringManager monitoringManager;

    @PostConstruct
    private void init() {
        monitoringManager.registerNotifierService(this);
    }

    @Override
    public boolean supportsNotifier(Notifier notifier) {
        return notifier instanceof URLNotifier;
    }

    @Override
    public void notifyMonitoring(Notifier notifier, MonitoringNotification notification) {
        URLNotifier urlNotifier = (URLNotifier) notifier;
        urlNotifier.getAddresses().forEach(url -> {
            sendNotification(urlNotifier, notification, url);
        });
    }

    private void sendNotification(URLNotifier notifier, MonitoringNotification notification, String url) {
        CloseableHttpClient client = HttpClients.custom()
                .disableCookieManagement()
                .build();
        try {
            CloseableHttpResponse response;
            try {
                HttpPost httpPost = new HttpPost(url);
                httpPost.setConfig(RequestConfig.copy(RequestConfig.DEFAULT)
                        .setSocketTimeout(2000)
                        .setConnectTimeout(2000)
                        .setConnectionRequestTimeout(2000)
                        .build());

                List<NameValuePair> urlParameters = new ArrayList<>();
                urlParameters.add(new BasicNameValuePair("timestamp", String.valueOf(notification.getTimestamp().getTime())));
                urlParameters.add(new BasicNameValuePair("name", notification.getMonitoringName()));
                urlParameters.add(new BasicNameValuePair("sentAt", String.valueOf(new Date().getTime())));

                if (notification.getResult() != null) {
                    Result result = notification.getResult();
                    urlParameters.add(new BasicNameValuePair("resultId", result.getId()));
                    urlParameters.add(new BasicNameValuePair("resultName", result.getName()));
                    urlParameters.add(new BasicNameValuePair("resultStatus", result.getStatus().name()));
                    urlParameters.add(new BasicNameValuePair("resultSeverity", result.getSeverity().name()));
                    urlParameters.add(new BasicNameValuePair("resultStartedAt", String.valueOf(result.getStartedAt().getTime())));
                    urlParameters.add(new BasicNameValuePair("resultLength", String.valueOf(result.getLength())));
                    if (result.getErrors().size() > 0) {
                        urlParameters.add(new BasicNameValuePair("resultErrors", String.join("\\n", result.getErrors())));
                    }
                }

                HttpEntity postParams = new UrlEncodedFormEntity(urlParameters);
                httpPost.setEntity(postParams);

                response = client.execute(httpPost);
            } catch (IOException e) {
                throw new RuntimeException("Error when sending notification to url " + url, e);
            }

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Wrong response code " + response.getStatusLine().getStatusCode() + " from notification on url " + url);
            }
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                throw new RuntimeException("Error when closing http client for url notification on " + url);
            }
        }

        LOGGER.info("URL notification sent to url " + url);
    }

}
