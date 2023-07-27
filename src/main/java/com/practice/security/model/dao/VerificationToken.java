package com.practice.security.model.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.security.model.dto.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class VerificationToken {

    public static final int EXPIRATION_TIME = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private Date expirationTime;

    @OneToOne
    @JoinColumn(name = "user_id",
    nullable = false,
    foreignKey = @ForeignKey(name = "FK_USER_VERIFY_TOKEN"))
    private UserDao  user;


    public VerificationToken(User user,String token) {
        super();
        ObjectMapper objectMapper = new ObjectMapper();
        UserDao userDao = objectMapper.convertValue(user,UserDao.class);
        this.token = token;
        this.user = userDao;
        this.expirationTime = calculateExpirationDate();
    }

    public VerificationToken(String token){
        this.token = token;
        this.expirationTime = calculateExpirationDate();
    }

    private Date calculateExpirationDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE,EXPIRATION_TIME);
        return new Date(calendar.getTime().getTime());
    }
}
