package com.lcwd.rating.controllers;

import com.lcwd.rating.entities.Rating;
import com.lcwd.rating.services.RatingService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ratings")
public class RatingControllers
{
    @Autowired
    private RatingService ratingService;
    @PreAuthorize("hasAuthority('Admin')")
    @PostMapping("/createRating")
    public ResponseEntity<Rating>create(@RequestBody Rating rating)
    {
        return ResponseEntity.status(HttpStatus.CREATED).body(ratingService.create(rating));
    }
    @PreAuthorize("hasAuthority('SCOPE_internal') || hasAuthority('Admin')")
    @GetMapping("/getAllRatings")
    public ResponseEntity<List<Rating>>getRatings()
    {
        return ResponseEntity.ok(ratingService.getRating());
    }
    @PreAuthorize("hasAuthority('SCOPE_internal') || hasAuthority('Admin')")
    @GetMapping("/getRatingByUserid/{userId}")
    public ResponseEntity<List<Rating>>getRatingByUserId(@PathVariable String userId)
    {
        return ResponseEntity.ok(ratingService.getRatingByUserId(userId));
    }
    @GetMapping("/getRatingByHotelId/{hotelId}")
    public ResponseEntity<List<Rating>>getRatingByHotelId(@PathVariable String hotelId)
    {
        return ResponseEntity.ok(ratingService.getRatingByHotelId(hotelId));
    }
}
