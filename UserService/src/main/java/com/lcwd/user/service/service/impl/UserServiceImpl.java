package com.lcwd.user.service.service.impl;

import com.lcwd.user.service.entities.Hotel;
import com.lcwd.user.service.entities.Rating;
import com.lcwd.user.service.entities.User;
import com.lcwd.user.service.exceptions.ResourceNotFoundException;
import com.lcwd.user.service.external.services.HotelService;
import com.lcwd.user.service.repository.UserRepository;
import com.lcwd.user.service.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService
{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private HotelService hotelService;

    private Logger logger= LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User saveUser(User user) {
        //generate Unique UserId
       String raUuid= UUID.randomUUID().toString();
       user.setUserId(raUuid);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override

   public User getUser(String userId) {
        //get user from database with the help of user repository
      User user=userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User with the given Id is not found on Server  !!"+userId));
      //fetch rating of the above user from the Rating service
       Rating[] ratingsOfUser= restTemplate.getForObject("http://RATING-SERVICE/ratings/getRatingByUserid/"+userId, Rating[].class);
        logger.info("{} ",ratingsOfUser);

        List<Rating>ratings=Arrays.stream(ratingsOfUser).toList();

        List<Rating> ratingList = ratings.stream().map(rating ->{
           // ResponseEntity<Hotel>forEntity= restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/"+ rating.getRatingId(), Hotel.class);
            Hotel hotel=hotelService.getHotel(rating.getHotelId());
            //Logger Tracking
            //logger.info("response Status code:{} ",forEntity.getStatusCode());
            //Setting the Hotel
            rating.setHotel(hotel);
            //return the rating
            return rating;
        }).collect(Collectors.toList());

        user.setRatings(ratingList);

        return user;
    }
}
