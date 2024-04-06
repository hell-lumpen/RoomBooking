package org.mai.roombooking.controllers;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.mai.roombooking.dtos.UserDTO;
import org.mai.roombooking.entities.User;
import org.mai.roombooking.exceptions.UserNotFoundException;
import org.mai.roombooking.services.UserService;
import org.mai.roombooking.services.ValidationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {
    UserService userService;
    ModelMapper modelMapper;
    ValidationService validationService;

    /**
     * Функция получения персонала кафедры
     * @return список активных преподавателей и администраторов
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public List<UserDTO> getAll() {
        return userService.findAll().stream()
                .map(UserDTO::new)
                .toList();
    }

    /**
     * Метод для получения списка не заблокированных пользователей
     * @return Список не заблокированных пользователей
     */
    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public List<UserDTO> getActive() {
        return userService.findAll()
                .stream()
                .filter(userDTO -> !userDTO.getIsAccountLocked())
                .map(UserDTO::new)
                .toList();
    }

    /**
     * Метод реализующий поиск пользователей по части их имени
     * @param pattern - Часть имени пользователя, по которому происходит поиск
     * @return список найденных пользователей
     */
    @GetMapping("/get/{pattern}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public List<UserDTO> getLike(@NonNull @PathVariable String pattern) {
        return userService.findUsernameLike(pattern).stream()
                .map(UserDTO::new)
                .toList();
    }

    /**
     * Метод обновления данных о пользователях
     * @param userDTO dto пользователя содержащее поля открытые для редактирования
     * @return обновленные данные пользователя
     */
    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public UserDTO updateUser(@NonNull @RequestBody UserDTO userDTO) {
        validationService.validate(userDTO);
        User userFormDTO = modelMapper.map(userDTO, User.class);
        return new UserDTO(userService.updateUser(userFormDTO));
    }

    /**
     * Метод удаления пользователя из базы данных
     * @param userId идентификатор пользователя, которого нужно удалить
     */
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public void deleteUserById(@NonNull @PathVariable Long userId) {
        userService.deleteUser(userId);
    }

    /**
     * Метод реализующий поиск по идентификатору (уникальное поле)
     * @param userId идентификатор пользователя
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@NonNull @PathVariable Long userId) {
        User user = userService.findById(userId).orElseThrow(()-> new UserNotFoundException(userId));
        return ResponseEntity.ok(new UserDTO(user));
    }

    /**
     * Метод полчения авторизованного пользователя
     * @param user авторизованный пользователь
     * @return Данные авторизованного пользователя
     */
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getAuthorizedUser(@AuthenticationPrincipal @NonNull User user) {
        return ResponseEntity.ok(new UserDTO(user));
    }
}
