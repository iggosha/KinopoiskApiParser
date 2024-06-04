package bsu.edu.kinopoiskparser.service;

import bsu.edu.kinopoiskparser.entity.Movie;

import java.util.List;

public interface MovieAsyncService {

    void saveIfNotSaved(List<Movie> movieList);

    void saveIfNotSaved(Movie movie);
}