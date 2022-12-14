package org.uvt.MovieRental.service;

import org.uvt.MovieRental.dto.MovieDTO;
import org.uvt.MovieRental.entity.WaitList;
import org.uvt.MovieRental.repository.MovieRepository;
import org.uvt.MovieRental.repository.RentRepository;
import org.uvt.MovieRental.repository.UserRepository;
import org.uvt.MovieRental.repository.WaitListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class WaitListService {

    @Autowired
    private WaitListRepository repository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private RentRepository rentRepository;

    public List<WaitList> getAll() {
        return repository.findAll();
    }

    public ResponseEntity<List<MovieDTO>> getWaitListForUser(Long userId) {
        List<WaitList> waitList = repository.findWaitListByUser_Id(userId);
        List<MovieDTO> movieDTOS = new ArrayList<>();
        for (WaitList wait : waitList) {
            MovieDTO movieDTO = new MovieDTO();
            movieDTO.setId(wait.getId());
            movieDTO.setInfo(wait.getMovie().getInfo());
            movieDTO.setReturnDate(rentRepository.findLastEndDateMovieWasRented(wait.getMovie().getId()).plusDays(1));

            movieDTOS.add(movieDTO);
        }

        return new ResponseEntity<>(movieDTOS, HttpStatus.OK);
    }

    public ResponseEntity<WaitList> addWaitList(Long userId, Long movieId) {
        WaitList waitList = new WaitList();
        waitList.setUser(userRepository.findById(userId).orElse(null));
        waitList.setMovie(movieRepository.findById(movieId).orElse(null));

        waitList.setDate(rentRepository.findLastEndDateMovieWasRented(movieId));
        if (waitList.getUser() == null || waitList.getMovie() == null
                || waitList.getDate() == null || waitList.getDate().compareTo(LocalDate.now()) < 0) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(repository.save(waitList), HttpStatus.CREATED);
    }

    public ResponseEntity<?> deleteWaitList(Long id) {
        if (repository.findById(id).orElse(null) != null) {
            repository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
