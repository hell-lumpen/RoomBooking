package org.mai.roombooking.entities.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long userId;
    private String username;
    private String phoneNumber;
    private String fullName;
    private String role;
}
