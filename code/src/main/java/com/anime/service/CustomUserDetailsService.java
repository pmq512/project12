package com.anime.service;

import com.anime.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService {
    @Autowired
    private UserService userService;

    public User loadUserByUsername(String username) {
        return userService.getUserByUsername(username);
    }
}
