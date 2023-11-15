package com.czavala.springsecurityjwt.persistance.entities;

import com.czavala.springsecurityjwt.persistance.entities.util.Role;
import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // si role del user es nulo, no hacemos nada
        if (role == null) return null;
        // si los permisos del role son nulo, no hacemos nada
        if (role.getPermissions() == null) return null;

        return role.getPermissions().stream()
                .map(permission -> {
                    String permissionName = permission.name();
                    // crea un authority (permiso) basado en los permisos que tenga el role del user
                    return new SimpleGrantedAuthority(permissionName);
                })
                .collect(Collectors.toList()); // retorna lista con los permisos (authorities)
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
