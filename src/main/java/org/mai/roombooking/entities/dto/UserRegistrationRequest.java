package org.mai.roombooking.entities.dto;

import lombok.Data;

@Data
public class UserRegistrationRequest {
    private String username;
    private String phoneNumber;
    private String fullName;
    private String passwordHash;
    private String role;
}
