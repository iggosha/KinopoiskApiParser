package bsu.edu.kinopoiskparser.service.impl;

import bsu.edu.kinopoiskparser.client.KinopoiskApiClient;
import bsu.edu.kinopoiskparser.entity.ApiMoviePage;
import bsu.edu.kinopoiskparser.entity.Movie;
import bsu.edu.kinopoiskparser.exception.custom.ExternalApiException;
import bsu.edu.kinopoiskparser.exception.custom.MovieNotFoundInExternalApiException;
import bsu.edu.kinopoiskparser.repository.MovieRepository;
import bsu.edu.kinopoiskparser.service.MovieService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final KinopoiskApiClient kinopoiskApiClient;
    private final ObjectMapper objectMapper;

    @Override
    public Movie getById(Long id) {
        Movie movie = findWithDbById(id)
                .or(() -> findWithApiById(id))
                .orElseThrow(() -> new MovieNotFoundInExternalApiException("Could not find movie with id " + id));
        movieRepository.save(movie);
        return movie;
    }

    @Override
    public List<Movie> getByPage(int page, int limit) {
        List<String> requiredFieldNames = Arrays.stream(Movie.class.getDeclaredFields()).map(Field::getName).toList();
        ApiMoviePage apiMoviePage = kinopoiskApiClient.getByPage(page, limit, requiredFieldNames);
        List<Movie> movieList = apiMoviePage.getMovies();
        movieRepository.saveAll(movieList);
        return movieList;
    }

    @SneakyThrows
    private Optional<Movie> findWithApiById(Long id) {
        Optional<Movie> movie = Optional.empty();
        try {
            movie = kinopoiskApiClient.findMovieById(id);
        } catch (FeignException e) {
            handleRawExternalApiException(e);
        }
        return movie;
    }

    private Optional<Movie> findWithDbById(Long id) {
        return movieRepository.findById(id);
    }

    private void handleRawExternalApiException(FeignException e) throws JsonProcessingException {
        Map<String, Object> map = objectMapper.readValue(e.contentUTF8(), new TypeReference<>() {
        });
        throw new ExternalApiException(
                String.join(", ", (List<String>) map.get("message")),
                (int) map.get("statusCode")
        );
    }
}
