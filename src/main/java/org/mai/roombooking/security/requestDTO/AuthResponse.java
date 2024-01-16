package org.mai.roombooking.security.requestDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mai.roombooking.dtos.UserDTO;
import org.mai.roombooking.entities.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    UserDTO user;
    String token;
}
