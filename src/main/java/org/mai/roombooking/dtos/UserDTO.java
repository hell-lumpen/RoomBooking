package org.mai.roombooking.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.mai.roombooking.entities.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    @NotNull(message = "Id should not be empty")
    private Long id;

    @NotEmpty(message = "Full name should not be empty")
    @NotBlank(message = "The phone number should not be empty")
    private String fullName;

    @Pattern(regexp = "8\\d{10}")
    @NotBlank(message = "The phone number should not be empty")
    @NotNull(message = "The phone number should not be empty")
    private String phoneNumber;

    @NotNull(message = "Role should not be empty")
    private User.UserRole role;

    @NotNull(message = "Is account locked should not be empty")
    private Boolean isAccountLocked;

    public UserDTO(@NonNull User user) {
        fullName = user.getFullName();
        phoneNumber = user.getPhoneNumber();
        id = user.getUserId();
        role = user.getRole();
        isAccountLocked = user.getIsAccountLocked();
    }
}
