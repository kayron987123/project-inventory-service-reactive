package org.gad.inventory_service.config.security;

import lombok.*;
import org.gad.inventory_service.model.Role;
import org.gad.inventory_service.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserSecurity implements UserDetails{
    private String idUser;
    private String username;
    private String password;
    private Set<Role> roles;

    public static UserSecurity buildUserDetails(User user) {
        return UserSecurity.builder()
                .idUser(user.getIdUser())
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles())
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        var permissions = roles.stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(permission -> (GrantedAuthority) permission::getName)
                .toList();

        var roleAuthorities = roles.stream()
                .map(authority -> (GrantedAuthority) authority::getName)
                .toList();

        return Stream.concat(permissions.stream(), roleAuthorities.stream()).toList();
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
