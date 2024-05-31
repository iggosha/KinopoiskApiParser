package bsu.edu.kinopoiskparser.exception.custom;

public class MovieNotFoundInApiException extends RuntimeException {

    public MovieNotFoundInApiException(String message) {
        super(message);
    }
}
