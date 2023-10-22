package org.mai.roombooking.controllers;

import org.mai.roombooking.dtos.EmployeeDTO;
import org.mai.roombooking.entities.User;
import org.mai.roombooking.entities.UserRole;
import org.mai.roombooking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    UserRepository userRepository;

    /**
     * Функция получения персонала кафедры
     * @return список активных преподавателей и администраторов
     */
    @GetMapping("/getAll")
    public List<EmployeeDTO> getAll() {
        return userRepository.findAll().stream()
                .filter((user) -> user.getRole().equals(UserRole.TEACHER) || user.getRole().equals(UserRole.ADMINISTRATOR))
                .filter((User::isAccountNonLocked))
                .map((EmployeeDTO::new))
                .toList();
    }

}
