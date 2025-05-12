package com.nguyenvannhat.library.services.users;

import com.nguyenvannhat.library.constants.Constant;
import com.nguyenvannhat.library.entities.Function;
import com.nguyenvannhat.library.entities.Role;
import com.nguyenvannhat.library.entities.User;
import com.nguyenvannhat.library.exceptions.ApplicationException;
import com.nguyenvannhat.library.repositories.FunctionRepository;
import com.nguyenvannhat.library.repositories.RoleRepository;
import com.nguyenvannhat.library.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailServices implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final FunctionRepository functionRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                User user = getUser();
                List<Role> role = roleRepository.findByUserIdAndIsDeletedIsFalse(user.getId());
                List<Long> roleIds = role.stream().map(Role::getId).collect(Collectors.toList());
                List<Function> functions = functionRepository.findByRoleIdIn(roleIds);
                role.stream().forEach(role1 -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role1.getCode().toUpperCase().trim())));
                functions.stream().forEach(function1 -> authorities.add(new SimpleGrantedAuthority(function1.getCode().toUpperCase().trim())));
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

            private User getUser() {
                return userRepository.findByUserName(username).orElseThrow(
                        () -> {
                            log.error("User not found by username: " + username);
                            return new ApplicationException(Constant.ERROR_USER_NOT_FOUND);
                        }
                );
            }
        };
    }
}
