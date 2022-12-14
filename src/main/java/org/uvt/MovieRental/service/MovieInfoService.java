package org.uvt.MovieRental.service;

import org.uvt.MovieRental.dto.MovieDTO;
import org.uvt.MovieRental.entity.Movie;
import org.uvt.MovieRental.entity.MovieInfo;
import org.uvt.MovieRental.repository.MovieInfoRepository;
import org.uvt.MovieRental.repository.RentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MovieInfoService {
    @Autowired
    private MovieInfoRepository repository;

    @Autowired
    private RentRepository rentRepository;

    public List<MovieInfo> getAll() {
        return repository.findAll();
    }

    public ResponseEntity<List<MovieDTO>> getAllHomeMovies() {
        List<MovieInfo> allMovieInfos = repository.findAll();
        List<MovieDTO> movieDTOS = new ArrayList<>();
        for (MovieInfo allMovieInfo : allMovieInfos) {
            MovieDTO tempMovieDTO = new MovieDTO();
            tempMovieDTO.setInfo(allMovieInfo);
            tempMovieDTO.setAvailable(false);
            Long tempId = null;
            LocalDate tempDate = LocalDate.now().plusWeeks(7);
            for (Movie movie : allMovieInfo.getMoviesList()) {
                LocalDate lastDate = rentRepository.findLastEndDateMovieWasRented(movie.getId());
                if (lastDate == null) {
                    tempMovieDTO.setId(movie.getId());
                    tempMovieDTO.setAvailable(true);
                    break;
                }
                if (lastDate.compareTo(tempDate) < 0) {
                    tempId = movie.getId();
                    tempDate = lastDate;
                    if (lastDate.compareTo(LocalDate.now()) < 0) {
                        tempMovieDTO.setAvailable(true);
                        break;
                    }
                }
            }
            if (tempMovieDTO.getId() == null) {
                tempMovieDTO.setId(tempId);
            }
            movieDTOS.add(tempMovieDTO);
        }

        return new ResponseEntity<>(movieDTOS, HttpStatus.OK);
    }

    public Optional<MovieInfo> getById(Long id) {
        return repository.findById(id);
    }

    public void addMovieInfo(MovieInfo movieInfo) {
        repository.save(movieInfo);
    }

    public void deleteMovieInfo(Long id) {
        repository.deleteById(id);
    }

    public void updateMovieInfo(MovieInfo movieInfo) {
        if (repository.findById(movieInfo.getId()).isPresent())
            repository.save(movieInfo);
    }
}
