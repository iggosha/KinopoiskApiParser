package bsu.edu.kinopoiskparser.controller;

import bsu.edu.kinopoiskparser.entity.Movie;
import bsu.edu.kinopoiskparser.service.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/movies-api")
public class MovieController {


    private final MovieService movieService;

    @GetMapping("/{id}")
    public Movie getMovie(@PathVariable("id") Long id) {
        return movieService.getById(id);
    }

}

