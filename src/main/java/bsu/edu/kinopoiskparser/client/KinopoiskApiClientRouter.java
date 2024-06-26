package bsu.edu.kinopoiskparser.client;

import bsu.edu.kinopoiskparser.entity.ApiMoviePage;
import bsu.edu.kinopoiskparser.entity.Movie;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "KinopoiskApiClientRouter", url = "${kinopoisk-api.base-url}", configuration = FeignConfig.class)
public interface KinopoiskApiClientRouter {

    @GetMapping("/v1.4/movie/{id}")
    Optional<Movie> findById(@PathVariable("id") Long id);

    @GetMapping("/v1.4/movie")
    ApiMoviePage findByPage(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(value = "selectFields") List<String> selectFields
    );

    @GetMapping("/v1.4/movie/search")
    ApiMoviePage findByPageByName(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(value = "query") String name
    );
}


