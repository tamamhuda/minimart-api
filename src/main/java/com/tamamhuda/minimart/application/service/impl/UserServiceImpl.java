package com.tamamhuda.minimart.application.service.impl;

import com.tamamhuda.minimart.application.service.UserService;
import com.tamamhuda.minimart.common.exception.UnauthorizedException;
import com.tamamhuda.minimart.domain.entity.User;
import com.tamamhuda.minimart.domain.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getUserByUsername(String username) throws ResponseStatusException {

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Username not found."));

    }

    public User getUserByEmail(String email) throws ResponseStatusException {

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Email is not registered."));

    }

    public UserDetails getByUsernameOrEmail(String username) throws UnauthorizedException {
        try {

            return username.contains("@")
                    ? getUserByEmail(username)
                    : getUserByUsername(username);

        } catch (ResponseStatusException e) {
            throw new UnauthorizedException(e.getReason());
        }
    }

    public User createUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists.");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already taken.");
        }

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        return userRepository.save(user);
    }

    public UserDetails validateCredentials(String username, String password) throws UnauthorizedException {
        UserDetails user = getByUsernameOrEmail(username);
        boolean isPasswordValid = passwordEncoder.matches(password, user.getPassword());

        if (!isPasswordValid) {
            throw new UnauthorizedException("Invalid credentials.");
        }

        return user;
    }
}
