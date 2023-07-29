package com.practice.security.controller;

import com.practice.security.event.RegistrationCompleteEvent;
import com.practice.security.model.dao.UserDao;
import com.practice.security.model.dto.User;
import com.practice.security.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user, final HttpServletRequest request){
        User response = userService.registerUser(user);
        publisher.publishEvent(new RegistrationCompleteEvent(response,applicationUrl(request)));
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") String token){
        String result = userService.validateVerificationToken(token);
        if(result.equalsIgnoreCase("valid"))
            return "User Verified Successfully";
        else
            return "User verification failed";
    }


    private String applicationUrl(HttpServletRequest request) {

        return "http://" +
                request.getServerName() +
                ":" +
                request.getServerPort() +
                request.getContextPath();
    }
}
