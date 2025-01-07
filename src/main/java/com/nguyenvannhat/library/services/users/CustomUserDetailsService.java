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
        User user = userRepository.findByUserName(username).orElseThrow(
                () -> new ApplicationException(Constant.ERROR_WRONG_USER_NAME_PASSWORD)
        );
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                List<GrantedAuthority> authorities = new ArrayList<>();
                for (String roleName : getListRoleName(user)) {
                    authorities.add(new SimpleGrantedAuthority(roleName));
                }
                return authorities;
            }

            @Override
            public String getPassword() {
                return user.getPassword();
            }

            @Override
            public String getUsername() {
                return user.getUserName();
            }
        };
    }

    private List<String> getListRoleName(User user) {
        Role role = userRoleRepository.findRoleByUser(user).orElseThrow(
                () -> new RuntimeException("Role not found")
        );
        List<Function> functions = roleFunctionRepository.findByRole(role);
        List<String> roleName = new java.util.ArrayList<>(functions.stream().map(
                Function::getFunctionName).toList());
        roleName.add(role.getName());
        return roleName;
    }
}
