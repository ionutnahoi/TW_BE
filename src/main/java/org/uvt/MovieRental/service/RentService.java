package org.uvt.MovieRental.service;

import org.uvt.MovieRental.entity.Rent;
import org.uvt.MovieRental.repository.MovieRepository;
import org.uvt.MovieRental.repository.RentRepository;
import org.uvt.MovieRental.repository.UserRepository;
import org.uvt.MovieRental.repository.WaitListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static java.time.temporal.ChronoUnit.WEEKS;

@Service
public class RentService {

    @Autowired
    private RentRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private WaitListRepository waitListRepository;


    public List<Rent> getAll() {
        return repository.findAll();
    }

    public List<Rent> getActiveRents() {
        return repository.findActiveRents();
    }

    public ResponseEntity<Rent> addRent(Long userId, Long movieId, int period) {
        if (!repository.findIfMovieIsRented(movieId) && period > 0 && period <= 4) {
            Rent rent = new Rent();
            rent.setUser(userRepository.findById(userId).orElse(null));
            rent.setMovie(movieRepository.findById(movieId).orElse(null));
            if (rent.getUser() == null || rent.getMovie() == null) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            rent.setStartDate(LocalDate.now());
            rent.setEndDate(LocalDate.now().plusWeeks(period));
            Long waitListId = waitListRepository.findIdByIdUserAndIdMovie(userId, movieId);
            if (waitListId != null)
                waitListRepository.deleteById(waitListId);
            return new ResponseEntity<>(repository.save(rent), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Rent> extendPeriod(Long id, int period) {
        if (period > 0 && period <= 2) {
            Rent rent = repository.findById(id).orElse(null);
            if (rent.getEndDate().compareTo(LocalDate.now()) >= 0) {
                rent.setEndDate(rent.getEndDate().plusWeeks(period));
            }
            if (WEEKS.between(rent.getStartDate(), rent.getEndDate()) <= 6) {
                return new ResponseEntity<>(repository.save(rent), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    public void extendPeriod(Long userId, Long movieId, int period) {
        if (period > 0 && period <= 2) {
            Rent rent = repository.findRentByUserIdAndMovieId(userId, movieId);
            if (rent.getEndDate().compareTo(LocalDate.now()) >= 0) {
                rent.setEndDate(rent.getEndDate().plusWeeks(period));
            }
            if (WEEKS.between(rent.getStartDate(), rent.getEndDate()) <= 6) {
                repository.save(rent);
            }
        }
    }

    public LocalDate getLastEndDateMovieWasRented(Long movieId) {
        return repository.findLastEndDateMovieWasRented(movieId);
    }

    public ResponseEntity<LocalDate> getDateWhenMovieWillBeAvailable(Long movieId) {
        LocalDate availableDate = repository.findLastEndDateMovieWasRented(movieId).plusDays(1);
        if (availableDate.compareTo(LocalDate.now()) >= 0) {
            return new ResponseEntity<>(availableDate, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}
