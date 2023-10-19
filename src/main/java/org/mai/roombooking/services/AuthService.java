package org.mai.roombooking.services;

import jakarta.servlet.http.HttpServletRequest;
import org.mai.roombooking.entities.User;
import org.mai.roombooking.entities.dto.UserLoginRequest;
import org.mai.roombooking.entities.dto.UserRegistrationRequest;
import org.mai.roombooking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;

    @Autowired
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(UserRegistrationRequest registrationRequest) {
        User newUser = User.builder()
                .username(registrationRequest.getUsername())
                .phoneNumber(registrationRequest.getPhoneNumber())
                .fullName(registrationRequest.getFullName())
                .passwordHash(registrationRequest.getPasswordHash())
                .build();

        userRepository.save(newUser);
    }

    public String loginUser(UserLoginRequest loginRequest) {
        return null;
    }

    public void logoutUser(HttpServletRequest request) {

    }
}
