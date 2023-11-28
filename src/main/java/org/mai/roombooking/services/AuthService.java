package org.mai.roombooking.services;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.mai.roombooking.entities.User;
import org.mai.roombooking.entities.UserInfo;
import org.mai.roombooking.exceptions.UserNotFoundException;
import org.mai.roombooking.security.requestDTO.AuthResponse;
import org.mai.roombooking.security.requestDTO.UserLoginRequest;
import org.mai.roombooking.security.requestDTO.UserRegistrationRequest;
import org.mai.roombooking.repositories.UserRepository;
import org.mai.roombooking.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse registerUser(@NonNull UserRegistrationRequest registrationRequest) {

        UserInfo userInfo = UserInfo.builder()
                .username(registrationRequest.getUsername())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .password(registrationRequest.getPhoneNumber())
                .role(User.UserRole.ADMINISTRATOR)
                .isAccountLocked(false)
                .build();

        User user = User.builder()
                .fullName(registrationRequest.getUsername())
                .info(userInfo)
                .build();

        userRepository.save(user);
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("fullname", user.getFullName());
        extraClaims.put("role", user.getRole());
        var jwtToken = jwtService.generateToken(extraClaims, user);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthResponse loginUser(@NonNull UserLoginRequest loginRequest) {
        System.out.println("---1");
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        System.out.println("---1?2");
        var user = userRepository.findByUsername(loginRequest.getUsername());
        if (user.size() != 1) throw new UserNotFoundException(0L);
        System.out.println("---2");

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("fullName", user.get(0).getFullName());
        extraClaims.put("role", user.get(0).getRole());
        System.out.println("---3");
        var jwtToken = jwtService.generateToken(extraClaims, user.get(0));
        System.out.println("---4");

        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }
}
