package org.mai.roombooking.dtoTest;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.mai.roombooking.dtos.UserDTO;
import org.mai.roombooking.entities.User;
import org.mai.roombooking.exceptions.DTOValidationException;
import org.mai.roombooking.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.lang.NonNull;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.stream.Stream;


@SpringBootTest
@RunWith(SpringRunner.class)
//@RunWith(SpringJUnit4ClassRunner.class)
public class ValidationUserDTOTest {
    @Autowired
    ValidationUtils validationUtils;

    @Test
    public void ValidateUserDto() throws Exception {
        var user = UserDTO
                .builder()
                .id(1L)
                .role(User.UserRole.TEACHER)
                .fullName("Artem")
                .phoneNumber("89297016633")
                .isAccountLocked(false)
                .build();
        validationUtils.validationRequest(user);
    }

    @ParameterizedTest
    @MethodSource("dataProvider")
    public void ErrorValidateUserDto(UserDTO user) {
        Assertions.assertThrows(DTOValidationException.class,() -> validationUtils.validationRequest(user));
    }



    static @NonNull Stream<UserDTO> dataProvider() {
		return Stream.of(
                UserDTO.builder()
                    .id(1L)
                    .role(User.UserRole.TEACHER)
                    .phoneNumber("89297016633")
                    .build(),
                UserDTO
                    .builder()
                    .id(1L)
                    .role(User.UserRole.TEACHER)
                    .phoneNumber("89297016633")
                    .isAccountLocked(false)
                    .build(),
                UserDTO
                        .builder()
                        .id(1L)
                        .role(User.UserRole.TEACHER)
                        .fullName("Artem")
                        .phoneNumber("79297016633")
                        .isAccountLocked(false)
                        .build(),
                UserDTO
                        .builder()
                        .role(User.UserRole.TEACHER)
                        .fullName("Artem")
                        .phoneNumber("89297016633")
                        .isAccountLocked(false)
                        .build(),
                UserDTO
                        .builder()
                        .id(1L)
                        .fullName("Artem")
                        .phoneNumber("89297016633")
                        .isAccountLocked(false)
                        .build()
		);
	}
}
