package org.uvt.MovieRental.controller;

import org.uvt.MovieRental.entity.Rent;
import org.uvt.MovieRental.service.RentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("rents")
@CrossOrigin
public class RentController {
    @Autowired
    private RentService rentService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Rent> getAll() {
        return rentService.getAll();
    }

    @RequestMapping(value = "active", method = RequestMethod.GET)
    public List<Rent> getActive() {
        return rentService.getActiveRents();
    }

    @RequestMapping(params = {"userId", "movieId", "period"}, method = RequestMethod.POST)
    public ResponseEntity<Rent> addRent(@RequestParam Long userId, @RequestParam Long movieId, @RequestParam int period) {
        return rentService.addRent(userId, movieId, period);
    }

    @RequestMapping(params = {"id", "period"}, method = RequestMethod.PUT)
    public ResponseEntity<Rent> extendRent(@RequestParam Long id, @RequestParam int period) {
        return rentService.extendPeriod(id, period);
    }

    @RequestMapping(params = {"userId", "movieId", "period"}, method = RequestMethod.PUT)
    public void extendRent(@RequestParam Long userId, @RequestParam Long movieId, @RequestParam int period) {
        rentService.extendPeriod(userId, movieId, period);
    }

    @RequestMapping(params = {"movieId"}, method = RequestMethod.GET)
    public LocalDate getLastEndDateMovieWasRented(@RequestParam Long movieId) {
        return rentService.getLastEndDateMovieWasRented(movieId);
    }

    @RequestMapping(value = "availableDate", params = {"movieId"}, method = RequestMethod.GET)
    public ResponseEntity<LocalDate> getDateWhenMovieWillBeAvailable(@RequestParam Long movieId) {
        return rentService.getDateWhenMovieWillBeAvailable(movieId);
    }
}
