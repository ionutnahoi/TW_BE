package org.uvt.MovieRental.repository;

import org.uvt.MovieRental.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("SELECT b FROM Movie b WHERE b.owner.id <> :id")
    List<Movie> findMoviesFromOtherUsers(Long id);

    @Query("SELECT b FROM Movie b WHERE LOWER(b.info.title) LIKE LOWER(:search) OR LOWER(b.info.author) LIKE LOWER(:search) ")
    List<Movie> findMoviesSearched(String search);
}
