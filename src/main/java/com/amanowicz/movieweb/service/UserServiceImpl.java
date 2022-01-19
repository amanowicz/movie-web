package com.amanowicz.movieweb.service;

import com.amanowicz.movieweb.exception.UserAlreadyExistsException;
import com.amanowicz.movieweb.model.User;
import com.amanowicz.movieweb.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(String username, String password) {
        if (userRepository.findByUsername(username) != null) {
            throw new UserAlreadyExistsException(String.format("User with name: %s already exists", username));
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        userRepository.save(user);
    }

    @Override
    public void login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthenticationServiceException("User not authenticated!");
        }
    }
}
