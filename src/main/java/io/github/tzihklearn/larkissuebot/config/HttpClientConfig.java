package io.github.tzihklearn.larkissuebot.config;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author tzih
 * @version v1.0
 * @since 2024.01.05
 */
@Configuration
public class HttpClientConfig {

    @Bean
    public CloseableHttpClient defaultCloseableHttpClient() {
        return HttpClients.createDefault();
    }

}
