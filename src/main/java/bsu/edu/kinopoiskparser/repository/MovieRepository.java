package bsu.edu.kinopoiskparser.repository;

import bsu.edu.kinopoiskparser.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

}
