package wildan.learn.springsecurity.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SecurityUser implements UserDetails {

    private static final long serialVersionUID = -6690946490872875352L;

    private final User user;
    public SecurityUser(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        if(user.getUserAuthoryties().isEmpty() || user.getUserAuthoryties() == null ) {
            return null;
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        user.getUserAuthoryties().stream().map(UserAuthoryty::getName).toList()
                .forEach(a -> {
                    GrantedAuthority authority = a::toString;
                    grantedAuthorities.add(authority);

                });

        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
