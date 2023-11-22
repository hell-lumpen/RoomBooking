package org.mai.roombooking.controllers;

import lombok.NonNull;
import org.mai.roombooking.dtos.UserDTO;
import org.mai.roombooking.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@PreAuthorize("hasRole('ADMINISTRATOR')")
public class UserController {
    UserService userService;

    @Autowired
    UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/test")
    public String getTest() {
        return "string";
    }

    /**
     * Функция получения персонала кафедры
     * @return список активных преподавателей и администраторов
     */
    @GetMapping("/get/all")
    public List<UserDTO> getAll() {
        return userService.findAll();
    }

    /**
     * Метод для получения списка не заблокированных пользователей
     * @return Список не заблокированных пользователей
     */
    @GetMapping("/get/active")
    public List<UserDTO> getActive() {
        return userService.findAll()
                .stream()
                .filter(userDTO -> !userDTO.getIsAccountLocked())
                .toList();
    }

    /**
     * Метод реализующий поиск пользователей по части их имени
     * @param pattern - Часть имени пользователя, по которому происходит поиск
     * @return список найденных пользователей
     */
    @GetMapping("/get/{pattern}")
    public List<UserDTO> getLike(@NonNull @PathVariable String pattern) {
        return userService.findUsernameLike(pattern);
    }

    /**
     * Метод обновления данных о пользователях
     * @param request dto пользователя содержащее поля открытые для редактирования
     * @return обновленные данные пользователя
     */
    @PutMapping("/update")
    public UserDTO updateUser(@NonNull @RequestBody UserDTO request) {
        return userService.updateUser(request);
    }

    /**
     * Метод удаления пользователя из базы данных
     * @param userId идентификатор пользователя, которого нужно удалить
     */
    @DeleteMapping("/{userId}")
    public void deleteUserById(@NonNull @PathVariable Long userId) {
        userService.deleteUser(userId);
    }

    /**
     * Метод реализующий поиск по идентификатору (уникальное поле)
     * @param userId идентификатор пользователя
     */
    @GetMapping("/{userId}")
    public void getUserById(@NonNull @PathVariable Long userId) {
        userService.findById(userId);
    }

}
