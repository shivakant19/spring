package com.practice.security.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.security.event.RegistrationCompleteEvent;
import com.practice.security.model.dao.UserDao;
import com.practice.security.model.dao.VerificationToken;
import com.practice.security.model.dto.User;
import com.practice.security.repository.UserRepository;
import com.practice.security.repository.VerificationTokenRepository;
import com.practice.security.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Objects;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;


    @Override
    public User registerUser(User user) {
        UserDao userDao = objectMapper.convertValue(user, UserDao.class);
        userDao.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao = userRepository.save(userDao);
        user = objectMapper.convertValue(userDao, User.class);
        log.info("{}",user);
        publisher.publishEvent(new RegistrationCompleteEvent(user,"url"));
        return user;
    }

    @Override
    public String validateVerificationToken(String token) {
        VerificationToken verificationToken =
                verificationTokenRepository.findByToken(token);

        if(Objects.isNull(verificationToken))
            return "expired";

        UserDao user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        log.info("---------calender instance :: {}----------------------",cal);
        if((verificationToken.getExpirationTime().getTime() - cal.getTime().getTime()) <= 0){
            verificationTokenRepository.delete(verificationToken);
            return "expire";
        }

        return null;

    }

    @Override
    public void saveVerificationTokenForUser(String token, User user) {
        VerificationToken verificationToken = new VerificationToken(user,token);
        verificationTokenRepository.save(verificationToken);
    }
}
