package bsu.edu.kinopoiskparser.service.impl;

import bsu.edu.kinopoiskparser.client.KinopoiskApiClientRouter;
import bsu.edu.kinopoiskparser.entity.Movie;
import bsu.edu.kinopoiskparser.exception.custom.ExternalApiException;
import bsu.edu.kinopoiskparser.exception.custom.MovieNotFoundInExternalApiException;
import bsu.edu.kinopoiskparser.repository.MovieRepository;
import bsu.edu.kinopoiskparser.service.MovieAsyncService;
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
    private final KinopoiskApiClientRouter kinopoiskApiClientRouter;
    private final MovieAsyncService movieAsyncService;
    private final ObjectMapper objectMapper;

    @Override
    public Movie getById(Long id) {
        Movie movie = findWithDbById(id)
                .or(() -> findWithApiById(id))
                .orElseThrow(() -> new MovieNotFoundInExternalApiException("Could not find movie with id " + id));
        movieAsyncService.saveIfNotSaved(movie);
        return movie;
    }

    @Override
    public List<Movie> getByPage(int page, int limit) {
        List<String> requiredFieldNames = Arrays.stream(Movie.class.getDeclaredFields()).map(Field::getName).toList();
        List<Movie> movieList = findWithApiByPage(page, limit, requiredFieldNames);
        movieAsyncService.saveIfNotSaved(movieList);
        return movieList;
    }

    @Override
    public Movie getByName(String name) {
        Movie movie = movieRepository.findTopByNameContainingIgnoreCase(name)
                .or(() -> findWithApiByName(name))
                .orElseThrow(() -> new MovieNotFoundInExternalApiException("Could not find movie with name" + name));
        movieAsyncService.saveIfNotSaved(movie);
        return movie;
    }


    @Override
    public List<Movie> getByPageByName(int page, int limit, String name) {
        List<Movie> movieList = findWithApiByPageByName(page, limit, name);
        movieAsyncService.saveIfNotSaved(movieList);
        return movieList;
    }

    @SneakyThrows
    private Optional<Movie> findWithApiById(Long id) {
        Optional<Movie> movie = Optional.empty();
        try {
            movie = kinopoiskApiClientRouter.findById(id);
        } catch (FeignException e) {
            handleRawExternalApiException(e);
        }
        return movie;
    }

    @SneakyThrows
    private Optional<Movie> findWithApiByName(String name) {
        Optional<Movie> movie = Optional.empty();
        try {
            movie = Optional.ofNullable(
                    findWithApiByPageByName(1, 1, name)
                            .getFirst()
            );
        } catch (FeignException e) {
            handleRawExternalApiException(e);
        }
        return movie;
    }

    private List<Movie> findWithApiByPageByName(int page, int limit, String name) {
        return kinopoiskApiClientRouter.findByPageByName(page, limit, name).getMovies();
    }

    private List<Movie> findWithApiByPage(int page, int limit, List<String> requiredFieldNames) {
        return kinopoiskApiClientRouter.findByPage(page, limit, requiredFieldNames).getMovies();
    }

    private Optional<Movie> findWithDbById(Long id) {
        return movieRepository.findById(id);
    }

    private void handleRawExternalApiException(FeignException e) throws JsonProcessingException, ExternalApiException {
        Map<String, Object> map = objectMapper.readValue(e.contentUTF8(), new TypeReference<>() {
        });
        //noinspection unchecked
        throw new ExternalApiException(
                String.join(", ", (List<String>) map.get("message")),
                (int) map.get("statusCode")
        );
    }
}
