package org.uvt.MovieRental.controller;

import org.uvt.MovieRental.dto.MovieDTO;
import org.uvt.MovieRental.entity.Movie;
import org.uvt.MovieRental.entity.MovieInfo;
import org.uvt.MovieRental.entity.User;
import org.uvt.MovieRental.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;


    @RequestMapping(method = RequestMethod.GET)
    public List<User> getAll() {
        return userService.getAllUsers();
    }

    @RequestMapping(params = "id", method = RequestMethod.GET)
    public Object getById(@RequestParam("id") Long id) {

        return userService.getUserById(id).isPresent() ? userService.getUserById(id).get() :
                new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @RequestMapping(params = {"email", "password"}, method = RequestMethod.GET)
    public ResponseEntity<User> getUserByEmailAndPassword(@RequestParam("email") String email, @RequestParam("password") String password) {
        return userService.getUserByEmailAndPassword(email, password);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<User> addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void updateUser(@RequestBody User user) {
        userService.updateUser(user);
    }

    @RequestMapping(params = "id", method = RequestMethod.DELETE)
    public void deleteById(@RequestParam("id") Long id) {
        userService.deleteUser(id);
    }

    @RequestMapping(params = "id", method = RequestMethod.POST)
    public ResponseEntity<Movie> addMovie(@RequestParam("id") Long id, @RequestBody MovieInfo movieInfo) {
        return userService.addMovie(id, movieInfo);
    }

    @RequestMapping(params = "id", value = "moviesReturn", method = RequestMethod.GET)
    public String getMoviesReturnToOwner(@RequestParam("id") Long id) {
        return userService.getMoviesReturnToOwner(id);
    }

    @RequestMapping(params = "id", value = "myMovies", method = RequestMethod.GET)
    public ResponseEntity<List<MovieDTO>> getMyMovies(@RequestParam("id") Long id) {
        return userService.getMyMovies(id);
    }


    @RequestMapping(params = "id", value = "giveRentedMovies", method = RequestMethod.GET)
    public String getMoviesUserNeedToReturn(@RequestParam("id") Long id) {
        return userService.getMoviesUserNeedToReturn(id);
    }

    @RequestMapping(params = "id", value = "myRented", method = RequestMethod.GET)
    public ResponseEntity<List<MovieDTO>> getMyRented(@RequestParam("id") Long id) {
        return userService.getMyRented(id);
    }

}
