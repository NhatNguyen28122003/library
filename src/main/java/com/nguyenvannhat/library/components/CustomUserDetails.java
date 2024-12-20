package com.nguyenvannhat.library.components;

import com.nguyenvannhat.library.entities.Role;
import com.nguyenvannhat.library.entities.UserCustom;
import com.nguyenvannhat.library.repositories.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class CustomUserDetails implements UserDetails {
    private UserCustom userCustom;
    private UserRepository userRepository;

    public CustomUserDetails(UserCustom userCustom, UserRepository userRepository) {
        this.userCustom = userCustom;
        this.userRepository = userRepository;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        Role role = userRepository.getRoleByUserName(userCustom.getUsername());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        for (Object roleName : userRepository.getFunctionRoleName(role.getId())) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return userCustom.getPassword();
    }

    @Override
    public String getUsername() {
        return userCustom.getUsername();
    }
}
