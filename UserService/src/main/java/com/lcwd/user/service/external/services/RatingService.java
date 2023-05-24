package com.lcwd.user.service.external.services;

import com.lcwd.user.service.entities.Rating;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Service
@FeignClient(name="RATING-SERVICE")
public interface RatingService
{
    //get
    @GetMapping("/ratings/getAllRatings")
    public ResponseEntity<List<Rating>>getAllRatings();

    //post

  @PostMapping("/ratings/createRating")
    public ResponseEntity<Rating> createRating(Rating values);

    //put
    @PutMapping("/ratings/{ratingId}")
    public ResponseEntity<Rating> updateRating(@PathVariable("ratingId") String ratingId, Rating rating);
    @DeleteMapping("/ratings/{ratingId}")
    public ResponseEntity<Rating> deleteRating(@PathVariable String ratingId);
}
