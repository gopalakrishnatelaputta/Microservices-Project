package com.lcwd.user.service.controllers;

import com.lcwd.user.service.entities.User;
import com.lcwd.user.service.service.impl.UserServiceImpl;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.lcwd.user.service.entities.User.*;

@RestController
@RequestMapping("/users")
public class UserController
{
    @Autowired
    private UserServiceImpl userServiceImpl;

    private Logger logger= LoggerFactory.getLogger(UserController.class);

    @PostMapping("/addUser")
    public ResponseEntity<User> createUser(@RequestBody User user) {

        User user1 = this.userServiceImpl.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user1);
    }
    int retryCount=1;
    @GetMapping("/{userId}")

    //  @CircuitBreaker(name="ratingHotelBreaker",fallbackMethod = "ratingHotelFallbackMethod")
    @Retry(name="ratingHotelservice",fallbackMethod = "ratingHotelFallbackMethod")

    public ResponseEntity<User> getSingleUser(@PathVariable String userId) {
        logger.info("get Single User Handler :UserController");
        logger.info("retry count: {}",retryCount);
        retryCount++;
        User user = userServiceImpl.getUser(userId);
        return ResponseEntity.ok(user);

    }


    public ResponseEntity<User>ratingHotelFallbackMethod(String userId,Exception ex)
    {

        logger.info("FallBack is executed because service is down : ",ex.getMessage());
        User user;

        user = User.builder()
                .email("dummy@gmail.com")
                .name("Dummy")
                .about("this is for Circuit Breaker")
                .userId("12345")
                .build();
        return new ResponseEntity<>(user,HttpStatus.OK);

    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers() {

        List<User> allUser = userServiceImpl.getAllUser();
        return ResponseEntity.ok(allUser);
    }
}
