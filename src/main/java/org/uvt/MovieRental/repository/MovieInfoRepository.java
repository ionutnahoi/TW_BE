package org.uvt.MovieRental.repository;

import org.uvt.MovieRental.entity.MovieInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieInfoRepository extends JpaRepository<MovieInfo, Long> {
    MovieInfo findMovieInfoByTitle(String title);
}
