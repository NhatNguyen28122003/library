package com.nguyenvannhat.library.services.users;

import com.nguyenvannhat.library.constants.Constant;
import com.nguyenvannhat.library.entities.Function;
import com.nguyenvannhat.library.entities.Role;
import com.nguyenvannhat.library.entities.User;
import com.nguyenvannhat.library.exceptions.ApplicationException;
import com.nguyenvannhat.library.repositories.RoleFunctionRepository;
import com.nguyenvannhat.library.repositories.UserRepository;
import com.nguyenvannhat.library.repositories.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleFunctionRepository roleFunctionRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new UserDetails() {
            public User getUser() {
                return userRepository.findByUserName(username).orElseThrow(
                        () -> new ApplicationException(Constant.ERROR_USER_NOT_FOUND)
                );
            }
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                List<GrantedAuthority> authorities = new ArrayList<>();
                List<Role> roles = userRoleRepository.findRolesByUser(getUser());
                for (Role role : roles) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
                    List<Function> functions = roleFunctionRepository.findFunctionsByRole(role);
                    for (Function function : functions) {
                        authorities.add(new SimpleGrantedAuthority( function.getFunctionName()));
                    }
                }
                return authorities;
            }

            @Override
            public String getPassword() {
                return getUser().getPassword();
            }

            @Override
            public String getUsername() {
                return getUser().getUserName();
            }
        };
    }
}
