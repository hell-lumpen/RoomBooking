package org.mai.roombooking.repositories;

import org.mai.roombooking.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u, UserInfo uu where uu.username = :username")
    List<User> findByUsername(String username);
    List<User> findByFullNameLikeIgnoreCase(String partialName);

    Optional<User> findUserByFullName(String fullName);
}
