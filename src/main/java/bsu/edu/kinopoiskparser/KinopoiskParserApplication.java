package bsu.edu.kinopoiskparser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class KinopoiskParserApplication {

    public static void main(String[] args) {
        SpringApplication.run(KinopoiskParserApplication.class, args);
    }

}
