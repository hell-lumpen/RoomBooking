package org.mai.roombooking.controllers;

import org.mai.roombooking.security.requestDTO.UserLoginRequest;
import org.mai.roombooking.security.requestDTO.UserRegistrationRequest;
import org.mai.roombooking.security.requestDTO.AuthResponse;
import org.mai.roombooking.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;


    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody UserRegistrationRequest registrationRequest) {
        return ResponseEntity.ok(authService.registerUser(registrationRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser(@RequestBody UserLoginRequest loginRequest) {
        return ResponseEntity.ok(authService.loginUser(loginRequest));
    }

//    @PostMapping("/logout")
//    public ResponseEntity<String> logoutUser(HttpServletRequest request) {
//        authService.logoutUser(request);
//        return ResponseEntity.ok("User logged out successfully");
//    }

}
