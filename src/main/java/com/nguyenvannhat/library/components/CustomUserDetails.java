package com.nguyenvannhat.library.components;

import com.nguyenvannhat.library.entities.Role;
import com.nguyenvannhat.library.entities.UserCustom;
import com.nguyenvannhat.library.repositories.UserRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class CustomUserDetails implements UserDetails, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private transient UserCustom userCustom;
    private transient UserRepository userRepository;

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
        if (userRepository != null && userCustom != null) {
            Role role = userRepository.getRoleByUserName(userCustom.getUsername());
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
            for (Object roleName : userRepository.getFunctionRoleName(role.getId())) {
                authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName));
            }
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return userCustom != null ? userCustom.getPassword() : null;
    }

    @Override
    public String getUsername() {
        return userCustom != null ? userCustom.getUsername() : null;
    }
}
