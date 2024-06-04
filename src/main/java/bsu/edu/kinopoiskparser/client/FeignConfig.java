package bsu.edu.kinopoiskparser.client;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    public static final String X_API_KEY = "X-API-KEY";
    @Value("${kinopoisk-api.key}")
    private String apiKey;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            template.header("accept", "application/json");
            template.header(X_API_KEY, apiKey);
        };
    }
}


