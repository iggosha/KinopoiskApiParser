package bsu.edu.kinopoiskparser.service.impl;

import bsu.edu.kinopoiskparser.entity.Movie;
import bsu.edu.kinopoiskparser.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieAsyncServiceImplTest {

    private static Movie mockMovie;
    private static List<Movie> mockMovieList;
    @InjectMocks
    private MovieAsyncServiceImpl movieAsyncService;
    @Mock
    private MovieRepository movieRepository;

    @BeforeEach
    void setUp() {
        long id = 1L;
        String movieName = "movie";

        mockMovie = new Movie();
        mockMovie.setId(id);
        mockMovie.setName(movieName);

        mockMovieList = new ArrayList<>(List.of(mockMovie));
    }

    @Test
    void saveEntityIfNotSavedShouldSave() {
        when(movieRepository.existsById(anyLong())).thenReturn(false);
        when(movieRepository.save(any())).thenReturn(mockMovie);

        movieAsyncService.saveIfNotSaved(mockMovie);

        verify(movieRepository).save(mockMovie);
    }

    @Test
    void saveEntityIfNotSavedShouldNotSave() {
        when(movieRepository.existsById(anyLong())).thenReturn(true);

        movieAsyncService.saveIfNotSaved(mockMovie);

        verify(movieRepository, never()).save(mockMovie);
    }

    @Test
    void saveEntityListIfNotSavedShouldSave() {
        when(movieRepository.findByIdIn(anyList())).thenReturn(new ArrayList<>());
        when(movieRepository.saveAll(anyList())).thenReturn(mockMovieList);

        movieAsyncService.saveIfNotSaved(mockMovieList);

        verify(movieRepository).saveAll(mockMovieList);
    }

    @Test
    void saveEntityListIfNotSavedShouldNotSave() {
        when(movieRepository.findByIdIn(anyList())).thenReturn(mockMovieList);
        when(movieRepository.saveAll(anyList())).thenReturn(mockMovieList);

        movieAsyncService.saveIfNotSaved(mockMovieList);

        verify(movieRepository, never()).saveAll(mockMovieList);
    }
}