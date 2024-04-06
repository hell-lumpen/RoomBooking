package org.mai.roombooking.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "fullname", nullable = false)
    @Setter
    private String fullName;


    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "info")
    private UserInfo info;

    public enum UserRole {
        AUTHORISED,
        ADMINISTRATOR,
        TEACHER
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.info.getRole().name()));
    }

    @Override
    public String getPassword() {
        return this.info.getPassword();
    }

    @Override
    public String getUsername() {
        return this.info.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.info.getIsAccountLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getPhoneNumber() {
        return (info != null) ? info.getPhoneNumber() : null;
    }

    public Boolean getIsAccountLocked(){
        return (info != null) ? info.getIsAccountLocked() : true;
    }

    public UserRole getRole() { return (info != null) ? info.getRole() : null; }

    public void setPhoneNumber(String phoneNumber) { info.setPhoneNumber(phoneNumber); }
    public void setIsAccountLocked(boolean isAccountLocked) { info.setIsAccountLocked(isAccountLocked); }
    public void setRole(UserRole role) { info.setRole(role); }
}

