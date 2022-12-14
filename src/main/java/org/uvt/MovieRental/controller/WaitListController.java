package org.uvt.MovieRental.controller;


import org.uvt.MovieRental.dto.MovieDTO;
import org.uvt.MovieRental.entity.WaitList;
import org.uvt.MovieRental.service.WaitListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("waitLists")
@CrossOrigin
public class WaitListController {
    @Autowired
    private WaitListService waitListService;

    @RequestMapping(method = RequestMethod.GET)
    public List<WaitList> getAll() {
        return waitListService.getAll();
    }

    @RequestMapping(params = {"id"}, method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteWaitList(Long id) {
        return waitListService.deleteWaitList(id);
    }

    @RequestMapping(params = {"userId"}, method = RequestMethod.GET)
    public ResponseEntity<List<MovieDTO>> getWaitListForUser(@RequestParam Long userId) {
        return waitListService.getWaitListForUser(userId);
    }

    @RequestMapping(params = {"userId", "movieId"}, method = RequestMethod.POST)
    public ResponseEntity<WaitList> addWaitList(@RequestParam Long userId, @RequestParam Long movieId) {
        return waitListService.addWaitList(userId, movieId);
    }

}
