package bsu.edu.kinopoiskparser.exception;

import bsu.edu.kinopoiskparser.exception.custom.ExternalApiException;
import bsu.edu.kinopoiskparser.exception.custom.MovieNotFoundInApiException;
import bsu.edu.kinopoiskparser.exception.custom.MovieNotFoundInDbException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MovieNotFoundInApiException.class)
    public ResponseEntity<?> handleMovieNotFoundInApiException(MovieNotFoundInApiException e) {
        ExceptionDetails exceptionDetails = new ExceptionDetails(new Date(), e.getMessage());
        return new ResponseEntity<>(exceptionDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MovieNotFoundInDbException.class)
    public ResponseEntity<?> handleMovieNotFoundInDbException(MovieNotFoundInDbException e) {
        ExceptionDetails exceptionDetails = new ExceptionDetails(new Date(), e.getMessage());
        return new ResponseEntity<>(exceptionDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<?> handleExternalApiException(ExternalApiException e) {
        ExceptionDetails exceptionDetails = new ExceptionDetails(new Date(), e.getMessage());
        HttpStatus httpStatus = HttpStatus.resolve(e.getStatusCode()) != null ? HttpStatus.resolve(e.getStatusCode()) : HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(exceptionDetails, httpStatus);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalExceptionHandler(Exception e) {
        ExceptionDetails exceptionDetails = new ExceptionDetails(new Date(), e.getMessage());
        return new ResponseEntity<>(exceptionDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


