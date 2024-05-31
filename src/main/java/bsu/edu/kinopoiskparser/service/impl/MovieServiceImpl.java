package bsu.edu.kinopoiskparser.service.impl;

import bsu.edu.kinopoiskparser.client.KinopoiskApiClient;
import bsu.edu.kinopoiskparser.entity.Movie;
import bsu.edu.kinopoiskparser.exception.custom.ExternalApiException;
import bsu.edu.kinopoiskparser.exception.custom.MovieNotFoundInApiException;
import bsu.edu.kinopoiskparser.exception.custom.MovieNotFoundInDbException;
import bsu.edu.kinopoiskparser.repository.MovieRepository;
import bsu.edu.kinopoiskparser.service.MovieService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Transactional
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final KinopoiskApiClient kinopoiskApiClient;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional(readOnly = true)
    public Movie getById(Long id) {
        Movie movie;
        try {
            movie = getMovieInDb(id);
        } catch (MovieNotFoundInDbException e) {
            movie = getMovieInApi(id);
        }
        return movie;
    }

    @SneakyThrows
    private Movie getMovieInApi(Long id) {
        Movie movie;
        try {
            movie = kinopoiskApiClient.findMovieById(id)
                    .orElseThrow(() -> new MovieNotFoundInApiException("Could not find movie with id " + id));
        } catch (FeignException e) {
            Map<String, Object> responseMap = objectMapper.readValue(e.contentUTF8(), new TypeReference<>() {
            });
            List<String> messages = (List<String>) responseMap.get("message");
            String errorMessage = String.join(", ", messages);
            int statusCode = (int) responseMap.get("statusCode");
            throw new ExternalApiException(errorMessage, statusCode);
        }
        return movie;
    }

    private Movie getMovieInDb(Long id) {
        return movieRepository
                .findById(id)
                .orElseThrow(() -> new MovieNotFoundInDbException("Could not find movie with id " + id));
    }
}
