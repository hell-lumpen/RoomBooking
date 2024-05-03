package org.mai.roombooking.services;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.mai.roombooking.dtos.UserDTO;
import org.mai.roombooking.entities.User;
import org.mai.roombooking.entities.UserInfo;
import org.mai.roombooking.exceptions.UserNotFoundException;
import org.mai.roombooking.repositories.UserInfoRepository;
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
    private final UserInfoRepository userInfoRepository;

    public AuthResponse registerUser(@NonNull UserRegistrationRequest registrationRequest) {

        UserInfo userInfo = UserInfo.builder()
                .username(registrationRequest.getUsername())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .phoneNumber(registrationRequest.getPhoneNumber())
                .role(User.UserRole.ADMINISTRATOR)
                .isAccountLocked(false)
                .build();

        User user = User.builder()
                .fullName(registrationRequest.getFullName())
                .info(userInfo)
                .build();
        userInfoRepository.save(userInfo);
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
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        var user = userRepository.findByUsername(loginRequest.getUsername());
        if (user.size() != 1) throw new UserNotFoundException(0L);

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("fullName", user.get(0).getFullName());
        extraClaims.put("role", user.get(0).getRole());
        var jwtToken = jwtService.generateToken(extraClaims, user.get(0));

        return AuthResponse.builder()
                .user(new UserDTO(user.get(0)))
                .token(jwtToken)
                .build();
    }
}
