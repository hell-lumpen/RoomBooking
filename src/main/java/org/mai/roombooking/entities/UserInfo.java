package org.mai.roombooking.entities;

import jakarta.persistence.*;
import lombok.*;

import java.lang.annotation.Target;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(name = "user_info")
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "phone_number", unique = true)
    @Setter
    private String phoneNumber;


    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "is_account_locked", nullable = false, columnDefinition = "boolean default false")
    @Setter
    private Boolean isAccountLocked;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    @Setter
    private User.UserRole role;
}
