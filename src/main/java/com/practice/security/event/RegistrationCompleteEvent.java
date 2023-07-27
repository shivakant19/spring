package com.practice.security.event;

import com.practice.security.model.dao.UserDao;
import com.practice.security.model.dto.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {


    private String url;
    private User user;


    public RegistrationCompleteEvent(User user, String url) {
        super(user);
        this.url = url;
        this.user = user;
    }
}
