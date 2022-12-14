package org.uvt.MovieRental.service;

import org.uvt.MovieRental.dto.MovieDTO;
import org.uvt.MovieRental.entity.Movie;
import org.uvt.MovieRental.entity.MovieInfo;
import org.uvt.MovieRental.entity.Rent;
import org.uvt.MovieRental.entity.User;
import org.uvt.MovieRental.repository.MovieInfoRepository;
import org.uvt.MovieRental.repository.MovieRepository;
import org.uvt.MovieRental.repository.RentRepository;
import org.uvt.MovieRental.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private MovieInfoRepository movieInfoRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private RentRepository rentRepository;


    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findUserByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return repository.findById(id);
    }

    public ResponseEntity<User> getUserByEmailAndPassword(String email, String password) {
        User user = repository.findUserByEmail(email);
        if (user != null) {
            if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
                return new ResponseEntity<>(user, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<User> addUser(User user) {
        User checkUser = repository.findUserByNameOrEmail(user.getName(), user.getEmail());
        if (checkUser == null) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            return new ResponseEntity<>(repository.save(user), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

    public void updateUser(User user) {
        if (repository.findById(user.getId()).isPresent())
            repository.save(user);
    }


    public ResponseEntity<Movie> addMovie(Long id, MovieInfo movieInfo) {
        HttpStatus status = HttpStatus.OK;
        User tempUser = repository.findById(id).orElse(null);
        if (tempUser == null || movieInfo.getTitle().equals("") || movieInfo.getGenre().equals("")) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        MovieInfo tempMovieInfo = movieInfoRepository.findMovieInfoByTitle(movieInfo.getTitle());
        if (tempMovieInfo == null) {
            tempMovieInfo = movieInfoRepository.save(movieInfo);
            status = HttpStatus.CREATED;
        }
        Movie movie = new Movie();
        movie.setInfo(tempMovieInfo);
        movie.setOwner(tempUser);
        return new ResponseEntity<>(movieRepository.save(movie), status);
    }

    public String getMoviesReturnToOwner(Long id) {
        StringBuilder result = new StringBuilder();
        List<Rent> rents = rentRepository.findMoviesReturnToOwner(id);

        for (Rent rent : rents) {
            result.append("Movie with id = ")
                    .append(rent.getMovie().getId()).append(", Title = ")
                    .append(rent.getMovie().getInfo().getTitle())
                    .append(" will be returned at ")
                    .append(rent.getEndDate()).append(" by ")
                    .append(rent.getUser().getName()).append("\n");
        }
        return result.toString();
    }

    public ResponseEntity<List<MovieDTO>> getMyMovies(Long id) {
        List<MovieDTO> movieDTOS = new ArrayList<>();
        List<Movie> moviesList = repository.findById(id).get().getMoviesList();
        for (Movie movie : moviesList) {
            MovieDTO movieDTO = new MovieDTO();
            movieDTO.setId(movie.getId());
            movieDTO.setInfo(movie.getInfo());
            Rent rent = movie.getRentedBy().stream().filter(r -> r.getEndDate().compareTo(LocalDate.now()) >= 0).findFirst().orElse(null);
            if (rent != null) {
                movieDTO.setUserName(rent.getUser().getName());
                movieDTO.setReturnDate(rent.getEndDate());
            }
            movieDTOS.add(movieDTO);
        }
        return new ResponseEntity<>(movieDTOS, HttpStatus.OK);
    }


    public String getMoviesUserNeedToReturn(Long id) {
        StringBuilder result = new StringBuilder("You rented:\n");
        List<Movie> movies = rentRepository.findMoviesRentedByUserId(id);
        for (Movie movie : movies) {
            result.append("Movie with id = ")
                    .append(movie.getId()).append(", Title = ")
                    .append(movie.getInfo().getTitle())
                    .append(" from ").append(movie.getOwner().getName())
                    .append(" and need to return it at ").append(movie.getRentedBy().get(movie.getRentedBy().size() - 1).getEndDate()).append("\n");
        }
        return result.toString();
    }

    public ResponseEntity<List<MovieDTO>> getMyRented(Long id) {
        List<MovieDTO> movieDTOS = new ArrayList<>();
        List<Rent> rents = rentRepository.findActiveRentsByUserId(id);
        for (Rent rent : rents) {
            MovieDTO movieDTO = new MovieDTO();
            movieDTO.setId(rent.getId());
            movieDTO.setInfo(rent.getMovie().getInfo());
            movieDTO.setUserName(rent.getMovie().getOwner().getName());
            movieDTO.setReturnDate(rent.getEndDate());
            movieDTOS.add(movieDTO);
        }
        return new ResponseEntity<>(movieDTOS, HttpStatus.OK);
    }
}

