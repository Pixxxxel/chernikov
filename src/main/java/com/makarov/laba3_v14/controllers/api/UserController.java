package com.makarov.laba3_v14.controllers.api;

import com.makarov.laba3_v14.models.User;
import com.makarov.laba3_v14.repositories.UserRepository;
import com.makarov.laba3_v14.services.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    protected UserRepository userRepository;

    protected UserDetailsService userDetailsService;

    @Autowired
    public UserController (
        UserRepository userRepository,
        UserDetailsService userDetailsService
    ) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/api/getUser")
    public User getUserInfo (@RequestParam("user_id") Long userId) {
        return this.userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @GetMapping("/api/getCurrentUser")
    public User getCurrentUser () {
        return userDetailsService.getCurrentUser();
    }
}
