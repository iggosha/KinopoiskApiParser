package bsu.edu.kinopoiskparser.controller;

import bsu.edu.kinopoiskparser.entity.Movie;
import bsu.edu.kinopoiskparser.service.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovieController.class)
class MovieControllerTest {

    private static long id;
    private static int page;
    private static int limit;
    private static Movie mockMovie;
    private static List<Movie> mockMovieList;
    private static String movieName;
    private static String mockMovieAsJsonString;
    private static String mockMovieListAsJsonString;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MovieService movieService;

    @SneakyThrows
    @BeforeEach
    void setUp() {
        page = 1;
        limit = 1;
        id = 1L;
        movieName = "movie";

        mockMovie = new Movie();
        mockMovie.setId(id);
        mockMovie.setName(movieName);
        mockMovieAsJsonString = objectMapper.writeValueAsString(mockMovie);

        mockMovieList = new ArrayList<>(List.of(mockMovie));
        mockMovieListAsJsonString = objectMapper.writeValueAsString(mockMovieList);
    }

    @Test
    void getById() throws Exception {
        when(movieService.getById(anyLong())).thenReturn(mockMovie);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/movies/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mockMovieAsJsonString))
                .andExpect(status().isOk());

        verify(movieService).getById(id);
    }

    @Test
    void getByPage() throws Exception {
        when(movieService.getByPage(anyInt(), anyInt())).thenReturn(mockMovieList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/movies")
                        .param("page", String.valueOf(page))
                        .param("limit", String.valueOf(limit))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mockMovieListAsJsonString))
                .andExpect(status().isOk());

        verify(movieService).getByPage(page, limit);
    }

    @Test
    void getByName() throws Exception {
        when(movieService.getByName(anyString())).thenReturn(mockMovie);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/movies/search")
                        .param("name", String.valueOf(movieName))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mockMovieAsJsonString))
                .andExpect(status().isOk());

        verify(movieService).getByName(movieName);
    }

    @Test
    void getByPageByName() throws Exception {
        when(movieService.getByPageByName(anyInt(), anyInt(), anyString())).thenReturn(mockMovieList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/movies/searchList")
                        .param("name", String.valueOf(movieName))
                        .param("page", String.valueOf(page))
                        .param("limit", String.valueOf(limit))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mockMovieListAsJsonString))
                .andExpect(status().isOk());

        verify(movieService).getByPageByName(page, limit, movieName);
    }
}