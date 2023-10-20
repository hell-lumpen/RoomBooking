package org.mai.roombooking.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mai.roombooking.entities.UserRole;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequest {
    private String username;
    private String phoneNumber;
    private String fullName;
    private String passwordHash;
}
