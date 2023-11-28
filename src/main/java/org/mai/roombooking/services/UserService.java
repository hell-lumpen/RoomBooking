package org.mai.roombooking.services;

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
public class UserService {

    UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO updateUser(@NonNull UserDTO request) {
        User user = userRepository.findById(request.getId())
                .orElseThrow(() -> new UserNotFoundException(request.getId()));

        user.setRole(request.getRole());
        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setIsAccountLocked(request.getIsAccountLocked());

        userRepository.save(user);

        return new UserDTO(user);
    }

    public void deleteUser(@NonNull Long userId) {
        userRepository.deleteById(userId);
    }

    public List<UserDTO> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserDTO::new)
                .toList();
    }

    public List<UserDTO> findUsernameLike(String like) {
        return userRepository.findByFullNameLikeIgnoreCase(like)
                .stream()
                .map(UserDTO::new)
                .toList();
    }

    public UserDTO findById(Long id) {
        return new UserDTO(userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException(id)));
    }

    public Optional<User> getUserByFullName(String fullName) {
        return userRepository.findUserByFullName(fullName);
    }

    public User createEmployee(String fullname) {
        return userRepository.save(User.builder().fullName(fullname).build());
    }
}
