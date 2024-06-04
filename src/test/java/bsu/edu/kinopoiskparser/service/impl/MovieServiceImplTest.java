package bsu.edu.kinopoiskparser.service.impl;

import bsu.edu.kinopoiskparser.client.KinopoiskApiClientRouter;
import bsu.edu.kinopoiskparser.entity.ApiMoviePage;
import bsu.edu.kinopoiskparser.entity.Movie;
import bsu.edu.kinopoiskparser.exception.custom.MovieNotFoundInExternalApiException;
import bsu.edu.kinopoiskparser.repository.MovieRepository;
import bsu.edu.kinopoiskparser.service.MovieAsyncService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {

    private long id;

    private int page;
    private int limit;
    private Movie mockMovie;
    private ApiMoviePage mockApiMoviePage;
    private List<Movie> mockMovieList;
    private String movieName;
    private static List<String> requiredFieldNames;
    @InjectMocks
    private MovieServiceImpl movieService;
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private MovieAsyncService movieAsyncService;
    @Mock
    private KinopoiskApiClientRouter kinopoiskApiClientRouter;

    @BeforeEach
    void setUp() {
        page = 1;
        limit = 1;
        id = 1L;
        movieName = "movie";

        requiredFieldNames = Arrays.stream(Movie.class.getDeclaredFields()).map(Field::getName).toList();

        mockMovie = new Movie();
        mockMovie.setId(id);
        mockMovie.setName(movieName);

        mockMovieList = new ArrayList<>(List.of(mockMovie));

        mockApiMoviePage = new ApiMoviePage();
        mockApiMoviePage.setMovies(mockMovieList);
    }

    @Test
    void getByIdShouldReturnEntityFromDb() {
        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(mockMovie));

        Movie movie = movieService.getById(id);

        assertEquals(mockMovie, movie);
        assertNotNull(movie);

        verify(movieRepository).findById(id);
        verify(kinopoiskApiClientRouter, never()).findById(id);
    }

    @Test
    void getByIdShouldReturnEntityFromApi() {
        when(movieRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(kinopoiskApiClientRouter.findById(anyLong())).thenReturn(Optional.of(mockMovie));

        Movie movie = movieService.getById(id);

        assertNotNull(movie);
        assertEquals(mockMovie, movie);

        verify(movieRepository).findById(id);
        verify(kinopoiskApiClientRouter).findById(id);
    }

    @Test
    void getByIdShouldThrowMovieNotFoundInExternalApiException() {
        when(movieRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(kinopoiskApiClientRouter.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(MovieNotFoundInExternalApiException.class, () -> movieService.getById(id));

        verify(movieRepository).findById(id);
        verify(kinopoiskApiClientRouter).findById(id);
    }

    @Test
    void getByPageShouldReturnEntityListFromApi() {
        when(kinopoiskApiClientRouter.findByPage(anyInt(), anyInt(), anyList())).thenReturn(mockApiMoviePage);

        List<Movie> movieList = movieService.getByPage(page, limit);

        assertEquals(mockMovieList, movieList);
        assertNotNull(movieList);

        verify(kinopoiskApiClientRouter).findByPage(page, limit, requiredFieldNames);
    }


    @Test
    void getByNameShouldReturnEntityFromDb() {
        when(movieRepository.findTopByNameContainingIgnoreCase(anyString())).thenReturn(Optional.of(mockMovie));

        Movie movie = movieService.getByName(movieName);

        assertEquals(mockMovie, movie);
        assertNotNull(movie);

        verify(movieRepository).findTopByNameContainingIgnoreCase(movieName);
        verify(kinopoiskApiClientRouter, never()).findByPageByName(page, limit, movieName);
    }

    @Test
    void getByNameShouldReturnEntityFromApi() {
        when(movieRepository.findTopByNameContainingIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(kinopoiskApiClientRouter.findByPageByName(anyInt(), anyInt(), anyString())).thenReturn(mockApiMoviePage);

        Movie movie = movieService.getByName(movieName);

        assertEquals(mockMovie, movie);
        assertNotNull(movie);

        verify(movieRepository).findTopByNameContainingIgnoreCase(movieName);
        verify(kinopoiskApiClientRouter).findByPageByName(page, limit, movieName);
    }

    @Test
    void getByNameShouldThrowMovieNotFoundInExternalApiException() {
        when(movieRepository.findTopByNameContainingIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(kinopoiskApiClientRouter.findByPageByName(anyInt(), anyInt(), anyString())).thenReturn(mockApiMoviePage);

        mockApiMoviePage.getMovies().set(0, null);

        assertThrows(MovieNotFoundInExternalApiException.class, () -> movieService.getByName(movieName));

        verify(movieRepository).findTopByNameContainingIgnoreCase(movieName);
        verify(kinopoiskApiClientRouter).findByPageByName(page, limit, movieName);
    }

    @Test
    void getByNameByPageShouldReturnEntityListFromApi() {
        when(kinopoiskApiClientRouter.findByPageByName(anyInt(), anyInt(), anyString())).thenReturn(mockApiMoviePage);

        List<Movie> movieList = movieService.getByPageByName(page, limit, movieName);

        assertEquals(mockMovieList, movieList);
        assertNotNull(movieList);

        verify(kinopoiskApiClientRouter).findByPageByName(page, limit, movieName);
    }
}