package org.uvt.MovieRental.service;

import org.uvt.MovieRental.entity.Movie;
import org.uvt.MovieRental.entity.Rent;
import org.uvt.MovieRental.repository.MovieInfoRepository;
import org.uvt.MovieRental.repository.MovieRepository;
import org.uvt.MovieRental.repository.RentRepository;
import org.uvt.MovieRental.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class MovieService {
    @Autowired
    private MovieRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MovieInfoRepository movieInfoRepository;

    @Autowired
    private RentRepository rentRepository;


    public void addMovie(Long userId, Long movieInfoId) {
        Movie movie = new Movie();
        movie.setOwner(userRepository.findById(userId).orElse(null));
        movie.setInfo(movieInfoRepository.findById(movieInfoId).orElse(null));
        repository.save(movie);
    }

    public List<Movie> getAll() {
        return repository.findAll();
    }

    public List<Movie> getAvailableMovies(Long id) {
        List<Movie> movies = repository.findMoviesFromOtherUsers(id);
        movies = movies.stream()
                .filter(movie -> movie.getRentedBy().stream().noneMatch(rent -> rent.getEndDate().compareTo(LocalDate.now()) >= 0))
                .collect(Collectors.toList());
        return movies;
    }

    public String searchForMovies(String search) {
        search = "%" + search + "%";
        List<Movie> movies = repository.findMoviesSearched(search);
        StringBuilder result = new StringBuilder();
        for (Movie movie : movies) {
            result.append("Movie with id = ")
                    .append(movie.getId())
                    .append(", Title = ")
                    .append(movie.getInfo().getTitle())
                    .append("  Genre = ")
                    .append(movie.getInfo().getGenre());
            if (rentRepository.findIfMovieIsRented(movie.getId())) {
                result.append(" will be available in: ").append(movie.getRentedBy().stream().map(Rent::getEndDate).max(Comparator.naturalOrder()).orElse(null)).append("\n");
            } else {
                result.append(" is available and can be rented from ").append(movie.getOwner().getName()).append("\n");
            }
        }
        return result.toString();
    }
}
