package org.mai.roombooking.services;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.mai.roombooking.dtos.UserDTO;
import org.mai.roombooking.entities.User;
import org.mai.roombooking.exceptions.UserNotFoundException;
import org.mai.roombooking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User updateUser(@NonNull User newUser) {
        return userRepository.save(newUser);
    }

    public void deleteUser(@NonNull Long userId) {
        userRepository.deleteById(userId);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findUsernameLike(String like) {
        return userRepository.findByFullNameLikeIgnoreCase(like);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByFullName(String fullName) {
        return userRepository.findUserByFullName(fullName);
    }

    public User createEmployee(String fullname) {
        return userRepository.save(User.builder().fullName(fullname).build());
    }
}
