package com.urlaki.Service;

import com.urlaki.DTO.RegisterRequest;
import com.urlaki.Repository.UserRepository;
import com.urlaki.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User register(RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already in use");
        }
        User user = User.builder().username(registerRequest.getUsername()).password(passwordEncoder.encode(registerRequest.getPassword())).role("ROLE_USER").build();
        return userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

    }




}
