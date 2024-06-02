package bsu.edu.kinopoiskparser.controller;

import bsu.edu.kinopoiskparser.entity.Movie;
import bsu.edu.kinopoiskparser.service.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/movies-api")
public class MovieController {


    private final MovieService movieService;

    @GetMapping("/{id}")
    public Movie getById(@PathVariable("id") Long id) {
        return movieService.getById(id);
    }

    @GetMapping("")
    public List<Movie> getByPage(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit
    ) {
        return movieService.getByPage(page, limit);
    }

    @GetMapping("/search")
    public Movie getByName(
            @RequestParam(value = "name") String name
    ) {
        return movieService.getByName(name);
    }

    @GetMapping("/searchList")
    public List<Movie> getByPageByName(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(value = "name") String name
    ) {
        return movieService.getByPageByName(page, limit, name);
    }
}

