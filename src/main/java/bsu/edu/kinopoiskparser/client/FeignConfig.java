package bsu.edu.kinopoiskparser.client;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            template.header("accept", "application/json");
            template.header("X-API-KEY", "YBYZT8C-893MX4Y-MNWKER5-A3MS8X5");
        };
    }
}


