package fido.currency.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fido.currency.security.UserRoles;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value = {"authorities",
        "role", "accountNonExpired", "credentialsNonExpired", "accountNonLocked"},allowSetters = true)
public class UserDto implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private String role;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return UserRoles.valueOf(role)
                .getAuthorities().stream()
                .map(a->new SimpleGrantedAuthority(a))
                .toList();
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
