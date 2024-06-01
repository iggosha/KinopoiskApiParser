package bsu.edu.kinopoiskparser.service;

import bsu.edu.kinopoiskparser.entity.Movie;

import java.util.List;

public interface MovieService {

    Movie getById(Long id);

    List<Movie> getByPage(int page, int limit);
}
