package com.nguyenvannhat.library.models;

import com.nguyenvannhat.library.models.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.*;

@Entity
@Table(name = "users")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "phone_number", unique = true, nullable = false)
    @Min(value = 5, message = "Phone number must be at least 5 characters long")
    private String phoneNumber;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "token")
    private String token;

    @Column(name = "is_active", nullable = false)
    private int isActive = 1;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private List<Post> posts;

    @OneToMany(mappedBy = "userComment", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return isActive == 1;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive == 1;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> auths = new ArrayList<>();
        for (Role role : roles) {
            auths.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        }
        return auths;
    }

    @Override
    public String getUsername() {
        return phoneNumber;
    }
}
