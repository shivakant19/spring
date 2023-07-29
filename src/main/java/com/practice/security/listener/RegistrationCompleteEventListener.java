package com.practice.security.listener;

import com.practice.security.event.RegistrationCompleteEvent;
import com.practice.security.model.dao.UserDao;
import com.practice.security.model.dto.User;
import com.practice.security.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    @Autowired
    private UserService userService;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        //create the verification token for the user
        User user = event.getUser();
        String token = UUID.randomUUID().toString();

        userService.saveVerificationTokenForUser(token,user);
        //send mail to user
        String url = event.getUrl() + "verifyRegistration?token=" + token;
        log.info("------------------Url -> {} -------",url);
    }
}
