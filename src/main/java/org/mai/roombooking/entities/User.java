package org.mai.roombooking.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Table(name = "Users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "phone_number", unique = true, nullable = false)
    @Setter
    private String phoneNumber;

    @Column(name = "fullname", nullable = false)
    @Setter
    private String fullName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "is_account_locked", nullable = false)
    @Setter
    private Boolean isAccountLocked;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    @Setter
    private UserRole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.isAccountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public enum UserRole
    {
        TECHNICIAN,
        ENGINEER,
        ADMINISTRATOR,
        DIRECTOR,
        TEACHER
    }
}

