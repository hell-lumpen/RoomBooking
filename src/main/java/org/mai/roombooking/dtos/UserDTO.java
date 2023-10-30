package org.mai.roombooking.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.mai.roombooking.entities.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @NotEmpty(message = "Full name should not be empty")
    private String fullName;

    @Pattern(regexp = "8\\d{10}")
    @NotBlank(message = "The phone number should not be empty")
    @NotEmpty(message = "The phone number should not be empty")
    private String phoneNumber;

    @NotEmpty(message = "Id should not be empty")
    private Long id;

    @NotEmpty(message = "Role should not be empty")
    private User.UserRole role;

    @NotEmpty(message = "Is account locked should not be empty")
    private Boolean isAccountLocked;

    public UserDTO(@NonNull User user) {
        fullName = user.getFullName();
        phoneNumber = user.getPhoneNumber();
        id = user.getUserId();
        role = user.getRole();
        isAccountLocked = user.getIsAccountLocked();
    }
}
