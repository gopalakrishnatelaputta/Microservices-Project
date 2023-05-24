package com.lcwd.rating.services;

import com.lcwd.rating.entities.Rating;

import java.util.List;

public interface RatingService
{
    //create
    Rating create (Rating rating);

    //get All rating
    List<Rating> getRating();

    //UserWise rating
    List<Rating>getRatingByUserId(String userId);

    //Get All By Hotel
    List<Rating>getRatingByHotelId(String hotelId);
}
