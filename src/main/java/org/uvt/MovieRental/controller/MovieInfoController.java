package org.uvt.MovieRental.controller;

import org.uvt.MovieRental.dto.MovieDTO;
import org.uvt.MovieRental.entity.MovieInfo;
import org.uvt.MovieRental.service.MovieInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("movieInfos")
@CrossOrigin
public class MovieInfoController {
    @Autowired
    private MovieInfoService movieInfoService;


    @RequestMapping(method = RequestMethod.GET)
    public List<MovieInfo> getAll() {
        return movieInfoService.getAll();
    }

    @RequestMapping(value = "home", method = RequestMethod.GET)
    public ResponseEntity<List<MovieDTO>> getAllHomeMovies() {
        return movieInfoService.getAllHomeMovies();
    }

    @RequestMapping(params = "id", method = RequestMethod.GET)
    public Object getById(@RequestParam("id") Long id) {
        return movieInfoService.getById(id).isPresent() ? movieInfoService.getById(id).get() :
                new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void addUser(@RequestBody MovieInfo movieInfo) {
        movieInfoService.addMovieInfo(movieInfo);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void updateUser(@RequestBody MovieInfo movieInfo) {
        movieInfoService.updateMovieInfo(movieInfo);
    }

    @RequestMapping(params = {"id"}, method = RequestMethod.DELETE)
    public void deleteById(@RequestParam("id") Long id) {
        movieInfoService.deleteMovieInfo(id);
    }

}
