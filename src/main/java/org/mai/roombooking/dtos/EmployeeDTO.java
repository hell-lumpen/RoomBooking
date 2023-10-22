package org.mai.roombooking.dtos;

import org.mai.roombooking.entities.User;

public class EmployeeDTO {
    String fullName;
    String phoneNumber;
    Long id;

    public EmployeeDTO(User user) {
        fullName = user.getFullName();
        phoneNumber = user.getPhoneNumber();
        id = user.getUserId();
    }
}
