package org.uvt.MovieRental.controller;


import org.uvt.MovieRental.entity.Movie;
import org.uvt.MovieRental.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("movies")
public class MovieController {

    @Autowired
    private MovieService movieService;


    @RequestMapping(method = RequestMethod.GET)
    public List<Movie> getAll() {
        return movieService.getAll();
    }

    @RequestMapping(params = {"id"}, value = "/available", method = RequestMethod.GET)
    public List<Movie> getAvailableMovies(@RequestParam Long id) {
        return movieService.getAvailableMovies(id);
    }

    @RequestMapping(params = {"search"}, value = "search", method = RequestMethod.GET)
    public String searchForMovies(@RequestParam String search) {
        return movieService.searchForMovies(search);
    }

    @RequestMapping(params = {"userId", "movieInfoId"}, method = RequestMethod.POST)
    public void addMovie(@RequestParam Long userId, @RequestParam Long movieInfoId) {
        movieService.addMovie(userId, movieInfoId);
    }
}
