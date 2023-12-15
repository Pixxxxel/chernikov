package com.makarov.laba3_v14.services;

import com.makarov.laba3_v14.enums.Role;
import com.makarov.laba3_v14.models.User;
import com.makarov.laba3_v14.responses.error.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.makarov.laba3_v14.repositories.UserRepository;

import javax.swing.text.html.Option;
import java.util.Optional;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public User registerUser (String username, String password) throws UserAlreadyExistsException {
        if (userRepository.findByUserName(username).isEmpty()) {
            User user = new User();

            user.setUserName(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(Role.USER);

            userRepository.save(user);

            return user;
        } else {
            throw new UserAlreadyExistsException("This username is already exists");
        }
    }
}
